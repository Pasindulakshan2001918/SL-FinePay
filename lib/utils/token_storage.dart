import 'package:shared_preferences/shared_preferences.dart';

class TokenStorage {
  static Future saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setString("token", token);
  }

  static Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString("token");
  }

  static Future clearToken() async {
    final prefs = await SharedPreferences.getInstance();
    prefs.remove("token");
  }
}