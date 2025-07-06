package api;

import api.colors.DataByYear;
import api.registration.SuccessfulRegistration;
import api.registration.UnSuccessfulRegistration;
import api.registration.UserRegistration;
import api.specification.Specification;
import api.users.UserData;
import api.users.UserTime;
import api.users.UserTimeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ApiPojoTest {

    private static final String URL = "https://reqres.in/";

    /**
     * Тест-кейс №1
     * 1. Используя сервис https://reqres.in/ получить список пользователей со второй страницы
     * 2. Убедиться, что id пользователей содержатся в их аватар
     * 3. Убедиться, что e-mail пользователей имеет окончание reqres.in
     */

    @Test
    @DisplayName("Аватары содержат Id пользователей")
    public void checkAvatarContainsIdTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusOK());
        /**
         * Первый способ:
         * Сравнение значений из экземпляров класса
         */

        List<UserData> users =
                // Given - When - Then
                // Предусловия
                given()
                // Выполняемые действия
                .when()
                .get("api/users?page=2")
                // Проверки
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        // проверка, что Id пользователя содержится в их аватар
        users.stream().forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        // проверка, что что e-mail пользователей имеет окончание reqres.in
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        /**
         * Второй способ:
         * Сравнение значений через списки
         */

        // получение списка с аватарами
        List<String> avatars = users.stream()
                .map(UserData::getAvatar)
                .collect(Collectors.toList());

        // получение списка с Id
        List<String> id = users.stream()
                .map(x -> x.getId().toString())
                .collect(Collectors.toList());

        // проверка сравнением двух списков
        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(id.get(i)));
        }

        // получение списка с email
        List<String> email = users.stream()
                .map(x -> x.getEmail().toString())
                .collect(Collectors.toList());

        // проверка, что что e-mail пользователей имеет окончание reqres.in
        for (int i = 0; i < email.size(); i++) {
            Assertions.assertTrue(email.get(i).contains("@reqres.in"));
        }
    }

    /**
     * Тест-кейс №2
     * 1. Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе
     * 2. Тест для успешной регистрации
     */

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void successfulRegistrationTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusOK());

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        UserRegistration userRegistration = new UserRegistration("eve.holt@reqres.in", "pistol");
        SuccessfulRegistration successfulRegistration = given()
                .body(userRegistration)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessfulRegistration.class);

        Assertions.assertNotNull(successfulRegistration.getId());
        Assertions.assertNotNull(successfulRegistration.getToken());
        Assertions.assertEquals(id, successfulRegistration.getId());
        Assertions.assertEquals(token, successfulRegistration.getToken());
    }

    /**
     * Тест-кейс №3
     * 1. Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе
     * 2. Тест для не успешной регистрации (отсутствие пароля)
     */

    @Test
    @DisplayName("Не успешная регистрация пользователя с отсутствием пароля")
    public void UnSuccessfuilRegistrationTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusBadRequest());

        UserRegistration userRegistration = new UserRegistration("sydney@fife", "");

        UnSuccessfulRegistration unSuccessfulRegistration = given()
                .body(userRegistration)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessfulRegistration.class);

        Assertions.assertEquals("Missing password", unSuccessfulRegistration.getError());
    }

    /**
     * Тест-кейс №4
     * 1. Используя сервис https://reqres.in/ убедиться, что операция LIST<RESOURCE> возвращает данные, отсортированные по годам
     */

    @Test
    @DisplayName("Сортировка по годам")
    public void sortByYearTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusOK());

        List<DataByYear> years = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", DataByYear.class);

        List<Integer> listYears = years.stream().map(DataByYear::getYear).collect(Collectors.toList());
        List<Integer> sortedListYears = listYears.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedListYears, listYears);
    }

    /**
     * Тест-кейс №5
     * 1. Используя сервис https://reqres.in/ удалить второго пользователя и сравнить статус код
     */

    @Test
    @DisplayName("Удаление пользователя")
    public void deleteUserTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusNoContent());

        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    /**
     * Тест-кейс №6
     * 1. Используя сервис https://reqres.in/ обновить информацию о пользователе и сравнить дату обновления с текущей датой на ПК
     */

    @Test
    @DisplayName("Сравнение времени ПК и сервера")
    // тест плавающий, могут быть погрешности
    public void checkPcAndServerDateTest() {
        Specification.initialSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecificationStatusOK());

        UserTime userTime = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(userTime)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);

        String regex = "(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");

        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex, ""));
    }
}
