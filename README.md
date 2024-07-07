## Jak działa system autoryzacji zdjęć?
- Zdjęcia są dostępne pod konkretnym endpointem w postaci binarnej z MediaType (JPEG,PNG).
- Frontend wysyła zapytanie GET do tego endpointu pobierając tą postać binarną
- Frontend następnie konwertuje binary array na obiekt typu blob w postaci URL np. blob:http://localhost:5173/ad0c9aee-adc8-4d6b-93ee-f57199480336
- Dostęp do tego url ma tylko zautoryzowany użytkownik