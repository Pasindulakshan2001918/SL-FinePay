class FineModel {
  final String referenceNumber;
  final String district;
  final String status;
  final String amount;
  final String offense;
  final String issuedDate;

  FineModel({
    required this.referenceNumber,
    required this.district,
    required this.status,
    this.amount = '0.00',
    this.offense = 'Not provided',
    this.issuedDate = '-',
  });

  factory FineModel.fromJson(Map<String, dynamic> json) {
    return FineModel(
      referenceNumber: json['referenceNumber'] ?? json['referenceNo'] ?? json['id'] ?? '',
      district: json['district'] ?? json['location'] ?? 'N/A',
      status: json['status'] ?? json['state'] ?? 'Unknown',
      amount: json['amount']?.toString() ?? json['fineAmount']?.toString() ?? '0.00',
      offense: json['offense'] ?? json['description'] ?? 'Not provided',
      issuedDate: json['issuedDate'] ?? json['date'] ?? '-',
    );
  }

  FineModel copyWith({
    String? referenceNumber,
    String? district,
    String? status,
    String? amount,
    String? offense,
    String? issuedDate,
  }) {
    return FineModel(
      referenceNumber: referenceNumber ?? this.referenceNumber,
      district: district ?? this.district,
      status: status ?? this.status,
      amount: amount ?? this.amount,
      offense: offense ?? this.offense,
      issuedDate: issuedDate ?? this.issuedDate,
    );
  }
}