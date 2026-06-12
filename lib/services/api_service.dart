import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  Future post(String url, Map body, {String? token}) async {
    return await http.post(
      Uri.parse(url),
      headers: {
        "Content-Type": "application/json",
        if (token != null) "Authorization": "Bearer $token",
      },
      body: jsonEncode(body),
    );
  }

  Future get(String url, {String? token}) async {
    return await http.get(
      Uri.parse(url),
      headers: {
        if (token != null) "Authorization": "Bearer $token",
      },
    );
  }
}