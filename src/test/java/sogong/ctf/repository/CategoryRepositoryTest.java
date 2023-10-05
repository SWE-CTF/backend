package sogong.ctf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void challengeInCategoryTest(){

        //given
        Category category = Category.builder()
                .name("Algorithms")
                .build();

        Challenge challenge1 = Challenge.builder()
                .title("Challenge 1")
                .content("Content 1")
                .memory(2.5f)
                .time(3.0f)
                .point(100)
                .build();

        Challenge challenge2 = Challenge.builder()
                .title("Challenge 2")
                .content("Content 2")
                .memory(4.0f)
                .time(2.0f)
                .point(150)
                .build();

        List<Challenge> challenges = Arrays.asList(challenge1,challenge2);

        category.getChallenges().addAll(challenges);
        challenges.forEach(challenge -> challenge.getCategories().add(category));


        //when
        Category saved_category = categoryRepository.save(category);
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);


        //then
        assertNotNull(saved_category.getId());
        List<Challenge> saved_challenges = challengeRepository.findAll();
        assertNotNull(saved_challenges);
        assertEquals(2, saved_challenges.size());


    }
}