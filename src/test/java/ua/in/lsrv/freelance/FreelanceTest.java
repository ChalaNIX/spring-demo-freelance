package ua.in.lsrv.freelance;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import ua.in.lsrv.freelance.dto.CommentDto;
import ua.in.lsrv.freelance.dto.JobDto;
import ua.in.lsrv.freelance.dto.UserDto;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.facade.CommentFacade;
import ua.in.lsrv.freelance.facade.JobFacade;
import ua.in.lsrv.freelance.facade.UserFacade;
import ua.in.lsrv.freelance.request.LoginRequest;
import ua.in.lsrv.freelance.request.SignUpRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FreelanceTest {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final Gson gson = new Gson();

    private final String URL = "http://localhost:8081/api/";

    private UserFacade userFacade = new UserFacade();

    private JobFacade jobFacade = new JobFacade();

    private CommentFacade commentFacade = new CommentFacade();

//    @Test
    public void appTest() throws IOException {
        Faker faker = new Faker();
        List<JsonJob> jsonJobs = parseHtml();
        Random random = new Random();

        List<User> users = new ArrayList<>();
        List<Job> jobs = new ArrayList<>();

        for (int i=0; i<20; i++) {
            String name = faker.name().firstName();
            String lastName = faker.name().lastName();
            String userName = String.format("%s.%s", name.toLowerCase(), lastName.toLowerCase());
            String password = "pass12345";
            System.out.println("Saving user: " + userName);

            User user = new User();
            user.setUsername(userName);
            user.setName(name);
            user.setLastname(lastName);
            user.setPassword(password);
            users.add(user);
            registerUser(user);
            String token = loginAsUser(user);
            updateUser(user, token);
        }

        int jobsNum = jsonJobs.size();

        for (int i=0; i<jobsNum; i++) {
            User user = users.get(random.nextInt(users.size()));

            JsonJob jsonJob = jsonJobs.get(0);

            Job job = new Job();
            job.setTitle(jsonJob.title);
            job.setDescription(jsonJob.description);
            job.setPrice(faker.number().numberBetween(1,19)*100);
            job.setUser(user);
            job.setCommentList(new ArrayList<>());

            jobs.add(job);

            String token = loginAsUser(user);
            createJob(job, token);
            jsonJobs.remove(jsonJob);
        }

        for (int i=0; i<10; i++) {
            User user = users.get(random.nextInt(users.size()));
            String token = loginAsUser(user);

            Job job = jobs.get(random.nextInt(jobs.size()));
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setMessage("I can do it, waiting for details");
            comment.setJob(job);
            comment.setCreateDate(LocalDateTime.now());

            createComment(comment, token, job.getId());
        }
    }

    private List<JsonJob> parseHtml() throws IOException {
        String file = new ClassPathResource("jobs.json", this.getClass().getClassLoader()).getFile().getAbsolutePath();
        StringBuilder jsonBuilder = new StringBuilder();

        Files.readAllLines(Paths.get(file)).forEach(jsonBuilder::append);

        Type type = new TypeToken<ArrayList<JsonJob>>() {}.getType();

        return gson.fromJson(jsonBuilder.toString(), type);
    }

    private void registerUser(User user) throws IOException {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(user.getUsername());
        signUpRequest.setPassword(user.getPassword());
        signUpRequest.setConfirmPassword(user.getPassword());

        String userJson = gson.toJson(signUpRequest);

        RequestBody body = RequestBody.create(userJson, JSON);

        Request registerRequest = new Request.Builder()
                .url(URL + "auth/signup")
                .post(body)
                .build();
        httpClient.newCall(registerRequest).execute();
    }

    private String loginAsUser(User user) throws IOException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        String userJson = gson.toJson(loginRequest);

        RequestBody body = RequestBody.create(userJson, JSON);

        Request registerRequest = new Request.Builder()
                .url(URL + "auth/signin")
                .post(body)
                .build();
        Response response = httpClient.newCall(registerRequest).execute();
        String responseBody = response.body().string();

        LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);
        return loginResponse.token;
    }

    private void updateUser(User user, String token) throws IOException {
        UserDto userDto = userFacade.userToDto(user);

        String json = gson.toJson(userDto);
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(URL + "user/update")
                .post(requestBody)
                .header("Authorization", token)
                .build();

        httpClient.newCall(request).execute();
    }

    private void createJob(Job job, String token) throws IOException {
        JobDto jobDto = jobFacade.jobToDto(job);
        String json = gson.toJson(jobDto);
        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(URL + "job/create")
                .post(requestBody)
                .header("Authorization", token)
                .build();

        String responseBody = httpClient.newCall(request).execute().body().string();
        JobDto createdJobDto = gson.fromJson(responseBody, JobDto.class);
        job.setId(createdJobDto.getId());
    }

    private void createComment(Comment comment, String token, long id) throws IOException {
        CommentDto commentDto = commentFacade.commentToDto(comment);
        String json = gson.toJson(commentDto);

        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(URL + String.format("comment/%d/create", id))
                .post(requestBody)
                .header("Authorization", token)
                .build();

        Response response = httpClient.newCall(request).execute();
        String s = response.message();
        System.out.println(s);
    }

    private static class LoginResponse {
        private String status;
        private String token;
    }

    private static class JsonJob {
        private String title, description;
    }
}