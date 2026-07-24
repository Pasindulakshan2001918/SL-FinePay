import 'package:flutter/material.dart';
import '../models/fine_model.dart';
import '../services/fine_service.dart';

class FineDetailsScreen extends StatefulWidget {
  final String referenceNumber;

  const FineDetailsScreen({
    super.key,
    required this.referenceNumber,
  });

  @override
  State<FineDetailsScreen> createState() => _FineDetailsScreenState();
}

class _FineDetailsScreenState extends State<FineDetailsScreen> {
  final fineService = FineService();

  FineModel? fine;
  bool isLoading = true;
  bool isPaying = false;

  @override
  void initState() {
    super.initState();
    loadFine();
  }

  Future<void> loadFine() async {
    final result = await fineService.getFine(widget.referenceNumber);

    if (!mounted) return;

    setState(() {
      fine = result;
      isLoading = false;
    });
  }

  Future<void> payFine() async {
    if (fine == null) return;

    setState(() => isPaying = true);

    final result = await fineService.payFine(fine!.referenceNumber);

    if (!mounted) return;

    setState(() => isPaying = false);

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(result['message'].toString())),
    );

    if (result['success'] == true) {
      setState(() {
        fine = fine!.copyWith(status: 'Paid');
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (fine == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Fine Details'),
        ),
        body: const Center(
          child: Text(
            'No fine found for this reference number.',
            style: TextStyle(
              fontSize: 18,
            ),
          ),
        ),
      );
    }

    final statusColor = fine!.status.toLowerCase().contains('paid')
        ? Colors.green
        : Colors.red;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Fine Details'),
        centerTitle: true,
      ),
      body: Container(
        width: double.infinity,
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Color(0xFFF8FAFF),
              Color(0xFFE0ECFF),
            ],
          ),
        ),
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: ConstrainedBox(
              constraints: const BoxConstraints(
                maxWidth: 500,
              ),
              child: Card(
                elevation: 8,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(30),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Icon(
                        Icons.receipt_long_rounded,
                        size: 80,
                        color: Color(0xFF2563EB),
                      ),

                      const SizedBox(height: 15),

                      const Text(
                        'Traffic Fine Details',
                        style: TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                        ),
                      ),

                      const SizedBox(height: 20),

                      Text(
                        fine!.referenceNumber,
                        textAlign: TextAlign.center,
                        style: const TextStyle(
                          fontSize: 28,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2563EB),
                        ),
                      ),

                      const SizedBox(height: 20),

                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 8,
                        ),
                        decoration: BoxDecoration(
                          color: statusColor.withOpacity(0.15),
                          borderRadius: BorderRadius.circular(50),
                        ),
                        child: Text(
                          fine!.status,
                          style: TextStyle(
                            color: statusColor,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),

                      const SizedBox(height: 30),

                      const Divider(),

                      _InfoRow(
                        label: "District",
                        value: fine!.district,
                      ),

                      _InfoRow(
                        label: "Offense",
                        value: fine!.offense,
                      ),

                      _InfoRow(
                        label: "Amount",
                        value: "Rs. ${fine!.amount}",
                      ),

                      _InfoRow(
                        label: "Issued Date",
                        value: fine!.issuedDate,
                      ),

                      const Divider(),

                      const SizedBox(height: 25),

                      SizedBox(
                        width: double.infinity,
                        height: 55,
                        child: ElevatedButton(
                          onPressed: isPaying ? null : payFine,
                          child: isPaying
                              ? const CircularProgressIndicator(
                                  color: Colors.white,
                                )
                              : const Text(
                                  "Pay Fine",
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  final String label;
  final String value;

  const _InfoRow({
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(
        vertical: 12,
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 130,
            child: Text(
              "$label :",
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: const TextStyle(
                fontSize: 18,
              ),
            ),
          ),
        ],
      ),
    );
  }
}