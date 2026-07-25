import 'dart:convert';
import 'package:http/http.dart' as http;
import '../core/api_constants.dart';

class RegisterService {
  Future<Map<String, dynamic>> register({
    required String username,
    required String password,
    required String email,
    required String role,
  }) async {
    final payloads = [
      {
        'username': username,
        'password': password,
        'email': email,
        'role': role,
      },
      {
        'user': {
          'username': username,
          
          'password': password,
          'email': email,
          'role': role,
        }
      },
      {
        'name': username,
        'password': password,
        'email': email,
        'role': role,
      },
    ];

    final endpoints = [
      '${ApiConstants.baseUrl}/api/auth/register',
      '${ApiConstants.baseUrl}/api/register',
      '${ApiConstants.baseUrl}/api/auth/signup',
    ];

    for (final endpoint in endpoints) {
      for (final payload in payloads) {
        try {
          final response = await http.post(
            Uri.parse(endpoint),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(payload),
          );

          if (response.statusCode >= 200 && response.statusCode < 300) {
            return {'success': true, 'message': 'Registration successful'};
          }

          if (response.body.isNotEmpty) {
            try {
              final decoded = jsonDecode(response.body);
              if (decoded is Map) {
                return {
                  'success': false,
                  'message': decoded['message']?.toString() ?? decoded['error']?.toString() ?? 'Registration failed',
                };
              }
            } catch (_) {}
          }
        } catch (e) {
          return {'success': false, 'message': 'Connection error: $e'};
        }
      }
    }

    return {'success': false, 'message': 'Registration failed. Check backend URL.'};
  }
}
