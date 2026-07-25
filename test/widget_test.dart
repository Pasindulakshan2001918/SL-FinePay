import 'package:flutter_test/flutter_test.dart';
import 'package:sl_finepay/main.dart';

void main() {
  testWidgets('app shows the login screen', (WidgetTester tester) async {
    await tester.pumpWidget(const MyApp());

    expect(find.text('SL FinePay'), findsOneWidget);
  });
}
