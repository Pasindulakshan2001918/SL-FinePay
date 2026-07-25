import 'package:flutter/material.dart';
import '../services/officer_service.dart';

class OfficerDashboardScreen extends StatefulWidget {
  const OfficerDashboardScreen({super.key});

  @override
  State<OfficerDashboardScreen> createState() =>
      _OfficerDashboardScreenState();
}

class _OfficerDashboardScreenState
    extends State<OfficerDashboardScreen> {
  final OfficerService service = OfficerService();

  final refController = TextEditingController();
  final driverPhoneController = TextEditingController();
  final districtController = TextEditingController();

  int selectedCategoryId = 1;

  bool isLoading = false;

  Future<void> createFine() async {
    if (refController.text.isEmpty ||
        driverPhoneController.text.isEmpty ||
        districtController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Please fill all fields"),
        ),
      );
      return;
    }

    setState(() {
      isLoading = true;
    });

    final result = await service.createFine(
      referenceNumber: refController.text.trim(),
      categoryId: selectedCategoryId,
      driverPhone: driverPhoneController.text.trim(),
      district: districtController.text.trim(),
    );

    setState(() {
      isLoading = false;
    });

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(result["message"]),
      ),
    );

    if (result["success"] == true) {
      refController.clear();
      driverPhoneController.clear();
      districtController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Officer Dashboard"),
        automaticallyImplyLeading: false,
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: ListView(
          children: [
            const Text(
              "Create Fine",
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),

            const SizedBox(height: 20),

            TextField(
              controller: refController,
              decoration: const InputDecoration(
                labelText: "Reference Number",
              ),
            ),

            const SizedBox(height: 15),

            DropdownButtonFormField<int>(
              value: selectedCategoryId,
              decoration: const InputDecoration(
                labelText: "Category",
              ),
              items: const [
                DropdownMenuItem(
                  value: 1,
                  child: Text("Speeding"),
                ),
                DropdownMenuItem(
                  value: 2,
                  child: Text("No License"),
                ),
                DropdownMenuItem(
                  value: 3,
                  child: Text("Signal Violation"),
                ),
                DropdownMenuItem(
                  value: 4,
                  child: Text("Parking Violation"),
                ),
              ],
              onChanged: (value) {
                setState(() {
                  selectedCategoryId = value!;
                });
              },
            ),

            const SizedBox(height: 15),

            TextField(
              controller: driverPhoneController,
              decoration: const InputDecoration(
                labelText: "Driver Phone",
              ),
              keyboardType: TextInputType.phone,
            ),

            const SizedBox(height: 15),

            TextField(
              controller: districtController,
              decoration: const InputDecoration(
                labelText: "District",
              ),
            ),

            const SizedBox(height: 30),

            ElevatedButton(
              onPressed: isLoading ? null : createFine,
              child: isLoading
                  ? const CircularProgressIndicator()
                  : const Text("Create Fine"),
            ),
          ],
        ),
      ),
    );
  }
}