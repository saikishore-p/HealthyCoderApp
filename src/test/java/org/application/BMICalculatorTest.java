package org.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    private String environment = "dev";

    @BeforeAll
    // applied to operations that are too expensive to be fun before each unit test
    // like database connections or starting servers
    static void beforeAll() {
        System.out.println("Before all unit tests");
    }

    @AfterAll
    // close database connections or stop servers
    static void afterAll() {
        System.out.println("After all unit tests");
    }

    @Nested
    class IsDietRecommendedTests{


        //    @Test
//    void should_ReturnTrue_When_DietRecommended(){
////        assertTrue(BMICalculator.isDietRecommended(81.2,1.65));
//
//        // given / Arrange
//        double weight = 89;
//        double height = 1.72;
//
//        // When / act
//        boolean recommended = BMICalculator.isDietRecommended(weight, height);
//
//        // Then / Assert
//        assertTrue(recommended);
//    }

        // converting above to parameterized tests

        // value source

//    @ParameterizedTest
//    @ValueSource(doubles={89.0, 95.0, 110.0})
//    void should_ReturnTrue_When_DietRecommended(Double coderWeight){
////        assertTrue(BMICalculator.isDietRecommended(81.2,1.65));
//
//        // given / Arrange
//        double weight = coderWeight;
//        double height = 1.72;
//
//        // When / act
//        boolean recommended = BMICalculator.isDietRecommended(weight, height);
//
//        // Then / Assert
//        assertTrue(recommended);
//    }


        // csv source

//    @ParameterizedTest(name = "weight={0}, height={1}")
//    @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.1, 1.78"})
//    void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight){
////        assertTrue(BMICalculator.isDietRecommended(81.2,1.65));
//
//        // given / Arrange
//        double weight = coderWeight;
//        double height = coderHeight;
//
//        // When / act
//        boolean recommended = BMICalculator.isDietRecommended(weight, height);
//
//        // Then / Assert
//        assertTrue(recommended);
//    }

        // csv file source

        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight){
//        assertTrue(BMICalculator.isDietRecommended(81.2,1.65));

            // given / Arrange
            double weight = coderWeight;
            double height = coderHeight;

            // When / act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // Then / Assert
            assertTrue(recommended);
        }

        @Test
        void should_ReturnFalse_When_DietNotRecommended(){

            // given / Arrange
            double weight = 50;
            double height = 1.92;

            // When / act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // Then / Assert
            assertFalse(recommended);

        }

        @Test
        void should_ThrowArithmeticException_When_HeightZero(){

            // given / Arrange
            double weight = 50;
            double height = 0.0;

            // When / act
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            // Then / Assert
            assertThrows(ArithmeticException.class, executable);
        }

    }


    @Nested
    @DisplayName("{}{}{} sample inner class display name")
    class FindCoderWithWorstBMITests{


        @Test
        @DisplayName(">>>>> sample method display name")
//        @Disabled // to ignore this particular test and it will be skipped and not be executed
        @DisabledOnOs(OS.MAC)
        void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty(){
            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            // when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertAll (
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn1Ms_when_CoderListHas10000Elements(){

            // assumptionms
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            // given
            List<Coder> coders = new ArrayList<>();
            for(int i=0; i < 10000; i++){
                coders.add(new Coder(1.0+i, 10.0+i));
            }
            // when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertTimeout(Duration.ofMillis(5), executable);
        }

        @Test
        void should_ReturnNullWorstBMI_When_CoderListEmpty(){
            // given
            List<Coder> coders = new ArrayList<>();

            // when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertNull(coderWorstBMI);
        }


    }



    @Nested
    class GetBMIScoresTests{
        @Test
        void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty(){
            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};

            // when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            // then
            assertArrayEquals(expected, bmiScores);
        }
    }


}