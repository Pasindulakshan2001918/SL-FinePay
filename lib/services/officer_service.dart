import 'dart:convert';
import 'package:http/http.dart' as http;
import '../core/api_constants.dart';
import '../utils/token_storage.dart';

class OfficerService {
  Future<Map<String, dynamic>> createFine({
    required String referenceNumber,
    required int categoryId,
    required String driverPhone,
    required String district,
  }) async {
    try {
      final token = await TokenStorage.getToken();

      final response = await http.post(
        Uri.parse('${ApiConstants.baseUrl}/api/fines/create'),
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer $token",
        },
        body: jsonEncode({
          "referenceNumber": referenceNumber,
          "categoryId": categoryId,
          "driverPhone": driverPhone,
          "district": district,
        }),
      );

      if (response.statusCode == 200 ||
          response.statusCode == 201) {
        return {
          "success": true,
          "message": "Fine created successfully"
        };
      }

      return {
        "success": false,
        "message": response.body
      };
    } catch (e) {
      return {
        "success": false,
        "message": e.toString()
      };
    }
  }
}