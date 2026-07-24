import 'dart:convert';
import 'package:http/http.dart' as http;
import '../core/api_constants.dart';
import '../models/fine_model.dart';
import '../utils/token_storage.dart';

class FineService {
  Future<FineModel?> getFine(String referenceNumber) async {
    final token = await TokenStorage.getToken();

    final response = await http.get(
      Uri.parse('${ApiConstants.baseUrl}/api/fines/$referenceNumber'),
      headers: {
        'Content-Type': 'application/json',
        if (token != null && token.isNotEmpty) 'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final decoded = jsonDecode(response.body);
      final body = decoded is Map ? Map<String, dynamic>.from(decoded) : <String, dynamic>{};
      final data = body.containsKey('data') && body['data'] is Map
          ? Map<String, dynamic>.from(body['data'])
          : body;
      return FineModel.fromJson(data);
    }

    return null;
  }

  Future<Map<String, dynamic>> payFine(String referenceNumber) async {
    final token = await TokenStorage.getToken();

    try {
      final endpoints = [
        '${ApiConstants.baseUrl}/api/fines/$referenceNumber/pay',
        '${ApiConstants.baseUrl}/api/payments/$referenceNumber',
        '${ApiConstants.baseUrl}/api/fines/pay/$referenceNumber',
      ];

      final headersList = [
        {
          'Content-Type': 'application/json',
          if (token != null && token.isNotEmpty) 'Authorization': 'Bearer $token',
        },
        {
          'Content-Type': 'application/json',
          if (token != null && token.isNotEmpty) 'x-access-token': token,
        },
      ];

      for (final endpoint in endpoints) {
        for (final headers in headersList) {
          final response = await http.post(
            Uri.parse(endpoint),
            headers: headers,
          );

          if (response.statusCode == 200 || response.statusCode == 201) {
            return {'success': true, 'message': 'Fine paid successfully'};
          }

          if (response.statusCode != 403 && response.statusCode != 404) {
            if (response.body.isNotEmpty) {
              try {
                final decoded = jsonDecode(response.body);
                if (decoded is Map) {
                  return {
                    'success': decoded['success'] == true,
                    'message': decoded['message']?.toString() ?? decoded['error']?.toString() ?? 'Payment failed',
                    'statusCode': response.statusCode,
                  };
                }
              } catch (_) {}
            }
            return {'success': false, 'message': 'Payment failed (${response.statusCode})', 'statusCode': response.statusCode};
          }
        }
      }

      return {'success': false, 'message': 'Payment failed (403)'};
    } catch (e) {
      return {'success': false, 'message': 'Connection error: $e'};
    }
  }
}