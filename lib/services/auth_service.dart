import 'dart:convert';
import '../core/api_constants.dart';
import '../utils/token_storage.dart';
import 'api_service.dart';

class AuthService {
  final ApiService api = ApiService();

  Future<bool> login(String username, String password) async {
    final response = await api.post(
      '${ApiConstants.baseUrl}${ApiConstants.login}',
      {
        'username': username,
        'password': password,
      },
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      final decoded = jsonDecode(response.body);
      Map<String, dynamic> body = decoded is Map ? Map<String, dynamic>.from(decoded) : {};

      if (body.containsKey('data') && body['data'] is Map) {
        body = Map<String, dynamic>.from(body['data']);
      }

      final token = body['token'] ?? body['accessToken'] ?? body['jwt'] ?? body['access_token'];
      if (token is String && token.isNotEmpty) {
        await TokenStorage.saveToken(token);
        return true;
      }
    }

    return false;
  }
}