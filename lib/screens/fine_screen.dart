import 'package:flutter/material.dart';
import '../models/fine_model.dart';
import '../services/fine_service.dart';

class FineScreen extends StatelessWidget {
  FineScreen({super.key, required this.data});

  final FineModel data;
  final service = FineService();

  void pay(BuildContext context) async {
    final result = await service.payFine(data.referenceNumber);

    if (!context.mounted) return;

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(result['message'].toString())),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Fine Details")),
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("Ref: ${data.referenceNumber}"),
            Text("District: ${data.district}"),
            Text("Status: ${data.status}"),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () => pay(context),
              child: Text("Pay Fine"),
            )
          ],
        ),
      ),
    );
  }
}