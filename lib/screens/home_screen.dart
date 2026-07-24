import 'package:flutter/material.dart';
import '../services/fine_service.dart';
import 'fine_screen.dart';

class HomeScreen extends StatelessWidget {
  HomeScreen({super.key});

  final controller = TextEditingController();
  final service = FineService();

  void search(BuildContext context) async {
    final data = await service.getFine(controller.text);

    if (!context.mounted || data == null) return;

    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => FineScreen(data: data)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Reference Fine Check")),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            TextField(
              controller: controller,
              decoration: const InputDecoration(
                labelText: "Reference Number",
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () => search(context),
              child: const Text("Search"),
            )
          ],
        ),
      ),
    );
  }
}