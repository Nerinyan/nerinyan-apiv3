package moe.nerinyan.apiv3.service.bancho

class Bancho4xxException(message: String) : RuntimeException(message)

class Bancho400Exception(message: String) : RuntimeException(message) // bad req

class Bancho401Exception(message: String) : RuntimeException(message) // unauthorized

class Bancho403Exception(message: String) : RuntimeException(message) // forbidden

class Bancho404Exception(message: String) : RuntimeException(message) // not found | 주로 비트맵셋 다운로드에서 발생함

class Bancho429Exception(message: String) : RuntimeException(message) // too many requests | 주로 비트맵셋 다운로드에서 발생함

class Bancho5xxException(message: String) : RuntimeException(message)
