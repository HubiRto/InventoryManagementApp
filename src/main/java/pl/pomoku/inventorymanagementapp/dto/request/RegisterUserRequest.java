package pl.pomoku.inventorymanagementapp.dto.request;

public record RegisterUserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
