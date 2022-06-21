package ua.in.lsrv.freelance.bootstrap;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.repository.CommentRepository;
import ua.in.lsrv.freelance.repository.JobRepository;
import ua.in.lsrv.freelance.repository.UserRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CommentRepository commentRepository;


    @Autowired
    private ResourceLoader resourceLoader;

    public DataInitializer(UserRepository userService,
                           JobRepository jobRepository,
                           CommentRepository commentRepository) {
        this.userRepository = userService;
        this.jobRepository = jobRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        List<JsonJob> jsonJobs = parseHtml();
        Random random = new Random();


        List<User> users = new ArrayList<>();

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

            userRepository.save(user);
            users.add(user);
        }

        List<Job> jobs = new ArrayList<>();
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

            jobRepository.save(job);

            jobs.add(job);
            jsonJobs.remove(jsonJob);
        }

        for (int i=0; i<10; i++) {
            User user = users.get(random.nextInt(users.size()));

            Job job = jobs.get(random.nextInt(jobs.size()));
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setMessage("I can do it, waiting for details");
            comment.setJob(job);
            comment.setCreateDate(LocalDateTime.now());

            commentRepository.save(comment);
        }
    }

    private List<JsonJob> parseHtml() throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();

        InputStream inputStream = resourceLoader.getResource("classpath:static/jobs.json").getInputStream();


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        jsonBuilder.append(bufferedReader.readLine());

        Type type = new TypeToken<ArrayList<JsonJob>>() {}.getType();

        return new Gson().fromJson(jsonBuilder.toString(), type);

    }

    private static class JsonJob {
        private String title, description;
    }
}