package GaussianNaiveBayes;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mmuaz on 8/14/17.
 */
public class MainPima {
    public static int calculateInheritanceFactor(String string){
        String[] array = string.split(";");
        for(int i = 0; i < array.length; i++){
            if(array[i].equalsIgnoreCase("father") || array[i].equalsIgnoreCase("mother")){
                return 1;
            }
        }
        return 0;
    }

    public static int calculateGenderFactor(String string){
        if(string.equalsIgnoreCase("male")){
            return 0;
        }
        return 1;
    }

    public static double calculateConditionalProbability(double value, double mean, double variance){
        double ans = 1 / Math.sqrt(2 * Math.PI * variance);
        ans *= Math.exp((-1) * (value - mean) * (value - mean) / (2 * variance));
        return ans;
    }

    public static void main(String[] args) throws Exception{
        ArrayList ageList = new ArrayList();
        ArrayList weightList = new ArrayList();
        ArrayList genderList = new ArrayList();
        ArrayList inheritanceList = new ArrayList();
        ArrayList classList = new ArrayList();

        int diabeticCount = 0;
        int preDiabeticCount = 0;
        int nonDiabeticCount = 0;

        // Metrices for diabetic
        double diabeticAgeSum = 0;
        double diabeticWeightSum = 0;
        double diabeticGenderSum = 0;
        double diabeticInheritanceSum = 0;

        double diabeticAgeSqSum = 0;
        double diabeticWeightSqSum = 0;
        double diabeticGenderSqSum = 0;
        double diabeticInheritanceSqSum = 0;

        // Metrices for pre diabetic
        double preDiabeticAgeSum = 0;
        double preDiabeticWeightSum = 0;
        double preDiabeticGenderSum = 0;
        double preDiabeticInheritanceSum = 0;

        double preDiabeticAgeSqSum = 0;
        double preDiabeticWeightSqSum = 0;
        double preDiabeticGenderSqSum = 0;
        double preDiabeticInheritanceSqSum = 0;

        // Metrices for non diabetic
        double nonDiabeticAgeSum = 0;
        double nonDiabeticWeightSum = 0;
        double nonDiabeticGenderSum = 0;
        double nonDiabeticInheritanceSum = 0;

        double nonDiabeticAgeSqSum = 0;
        double nonDiabeticWeightSqSum = 0;
        double nonDiabeticGenderSqSum = 0;
        double nonDiabeticInheritanceSqSum = 0;

        String inFileName = "survey_for_thesis.csv";
//        Scanner input = new Scanner(new File(inFileName));
        Scanner input = new Scanner(System.in);

/*
        // Test Data
        System.out.println("Age in years: ");
        double testAge = Double.valueOf(input.nextLine());
        System.out.println("Weight in kg: ");
        double testWeight = Double.valueOf(input.nextLine());
        System.out.println("0 if male, 1 if female: ");
        double testGender = Double.valueOf(input.nextLine());
        System.out.println("0 if parents do not have diabetic, 1 otherwise: ");
        double testInheritance = Double.valueOf(input.nextLine());

*/

        input.nextLine(); // Heading

        // Training Data input and learning
        while(input.hasNextLine()){
            String temp = input.nextLine();
//            System.out.println(temp);
            String store[] = temp.split(",");
//            if(!store[5].equals("")){ //If the sugar level is known
//                System.out.println("bingo");
                double age = Double.valueOf(store[0]); //age
                double weight = Double.valueOf(store[1]); //bmi
                int gender = Integer.valueOf(store[2]); //pregnant
                int inheritance = Integer.valueOf(store[3]); //glucose
                String className = store[5];

//            System.out.println(age + " " + weight + " " + gender + " " + inheritance);
//            System.out.println(store[0] + " " + store[1] + " " + store[2] + " " + store[3]);
//                Patient patient = new Patient(age, weight, gender, inheritance);
//                if(Double.valueOf(store[5]) < 9.0){
            if(age != 0 && weight != 0 && gender != 0 && inheritance != 0){
//                System.out.println("dkf");
                ageList.add(age);
                weightList.add(weight);
                genderList.add(gender);
                inheritanceList.add(inheritance);
                classList.add(className);
                if(store[5].equalsIgnoreCase("0")){
                    nonDiabeticCount++;
                    nonDiabeticAgeSum += age;
                    nonDiabeticAgeSqSum += (age * age);
                    nonDiabeticWeightSum += weight;
                    nonDiabeticWeightSqSum += (weight * weight);
                    nonDiabeticGenderSum += gender;
                    nonDiabeticGenderSqSum += (gender * gender);
                    nonDiabeticInheritanceSum += inheritance;
                    nonDiabeticInheritanceSqSum += (inheritance * inheritance);
                }
                else{
                    diabeticCount++;
                    diabeticAgeSum += age;
                    diabeticAgeSqSum += (age * age);
                    diabeticWeightSum += weight;
                    diabeticWeightSqSum += (weight * weight);
                    diabeticGenderSum += gender;
                    diabeticGenderSqSum += (gender * gender);
                    diabeticInheritanceSum += inheritance;
                    diabeticInheritanceSqSum += (inheritance * inheritance);
                }
            }
//            }


            /*age.add(store[3]);
            weight.add(store[4]);
            sugarLevel.add(store[5]);
            gene.add(geneConvert(store[6]));*/
//            count++;
        }

        double diabeticAgeMean = (diabeticAgeSum / diabeticCount);
        double diabeticAgeVariance = (diabeticAgeSqSum / diabeticCount) - (diabeticAgeMean);
        double diabeticWeightMean = (diabeticWeightSum / diabeticCount);
        double diabeticWeightVariance = (diabeticWeightSqSum / diabeticCount) - (diabeticWeightMean);
        double diabeticGenderMean = (diabeticGenderSum / diabeticCount);
        double diabeticGenderVariance = (diabeticGenderSqSum / diabeticCount) - (diabeticGenderMean);
        double diabeticInheritanceMean = (diabeticInheritanceSum / diabeticCount);
        double diabeticInheritanceVariance = (diabeticInheritanceSqSum / diabeticCount) - (diabeticInheritanceMean);

        double preDiabeticAgeMean = (preDiabeticAgeSum / preDiabeticCount);
        double preDiabeticAgeVariance = (preDiabeticAgeSqSum / preDiabeticCount) - (preDiabeticAgeMean);
        double preDiabeticWeightMean = (preDiabeticWeightSum / preDiabeticCount);
        double preDiabeticWeightVariance = (preDiabeticWeightSqSum / preDiabeticCount) - (preDiabeticWeightMean);
        double preDiabeticGenderMean = (preDiabeticGenderSum / preDiabeticCount);
        double preDiabeticGenderVariance = (preDiabeticGenderSqSum / preDiabeticCount) - (preDiabeticGenderMean);
        double preDiabeticInheritanceMean = (preDiabeticInheritanceSum / preDiabeticCount);
        double preDiabeticInheritanceVariance = (preDiabeticInheritanceSqSum / preDiabeticCount) - (preDiabeticInheritanceMean);

        double nonDiabeticAgeMean = (nonDiabeticAgeSum / nonDiabeticCount);
        double nonDiabeticAgeVariance = (nonDiabeticAgeSqSum / nonDiabeticCount) - (nonDiabeticAgeMean);
        double nonDiabeticWeightMean = (nonDiabeticWeightSum / nonDiabeticCount);
        double nonDiabeticWeightVariance = (nonDiabeticWeightSqSum / nonDiabeticCount) - (nonDiabeticWeightMean);
        double nonDiabeticGenderMean = (nonDiabeticGenderSum / nonDiabeticCount);
        double nonDiabeticGenderVariance = (nonDiabeticGenderSqSum / nonDiabeticCount) - (nonDiabeticGenderMean);
        double nonDiabeticInheritanceMean = (nonDiabeticInheritanceSum / nonDiabeticCount);
        double nonDiabeticInheritanceVariance = (nonDiabeticInheritanceSqSum / nonDiabeticCount) - (nonDiabeticInheritanceMean);


        System.out.println("Diabetic: " + diabeticCount + ", Pre: " + preDiabeticCount + ", Non: " + nonDiabeticCount);
        System.out.println("Feature\tDiabetic mean\tDiabetic variance\tPre mean\tPre variance\tNon mean\tNon variance");
        System.out.println("Age\t" + diabeticAgeMean + "\t" + diabeticAgeVariance + "\t" + preDiabeticAgeMean + "\t" + preDiabeticAgeVariance + "\t" + nonDiabeticAgeMean + "\t" + nonDiabeticAgeVariance);
        System.out.println("Weight\t" + diabeticWeightMean + "\t" + diabeticWeightVariance + "\t" + preDiabeticWeightMean + "\t" + preDiabeticWeightVariance + "\t" + nonDiabeticWeightMean + "\t" + nonDiabeticWeightVariance);



        int totalCount = diabeticCount + preDiabeticCount + nonDiabeticCount;
        int correctCount = 0;
        double errorSum = 0.0;

        double diabeticPrior = (double) diabeticCount / (diabeticCount + preDiabeticCount + nonDiabeticCount);
        double preDiabeticPrior = (double) preDiabeticCount / (diabeticCount + preDiabeticCount + nonDiabeticCount);
        double nonDiabeticPrior = (double) nonDiabeticCount / (diabeticCount + preDiabeticCount + nonDiabeticCount);

        int tp = 0, tn = 0, fp = 0, fn = 0;
        for(int i = 0; i < totalCount; i++){
            double testAge = (double) ageList.get(i);
            double testWeight = (double) weightList.get(i);
            int testGender = (int) genderList.get(i);
            int testInheritance = (int) inheritanceList.get(i);
            String testClass = classList.get(i).toString();
            double error = 0;

            System.out.println("Test #" + (i + 1) + ":");
            System.out.println();
            System.out.println("Age: " + testAge + ",  Weight: " + testWeight + ", Gender: " + testGender + ", Inheritance: " + testInheritance);

            // Test for diabetic
            double diabeticAgeConditional = calculateConditionalProbability(testAge, diabeticAgeMean, diabeticAgeVariance);
            double diabeticWeightConditional = calculateConditionalProbability(testWeight, diabeticWeightMean, diabeticWeightVariance);
            double diabeticGenderConditional = calculateConditionalProbability(testGender, diabeticGenderMean, diabeticGenderVariance);
            double diabeticInheritanceConditional = calculateConditionalProbability(testInheritance, diabeticInheritanceMean, diabeticInheritanceVariance);
            /*double diabeticGenderConditional = diabeticGenderSum / (double) diabeticCount;
            if(testGender == 0){
                diabeticGenderConditional = 1 - diabeticGenderConditional;
            }
            double diabeticInheritanceConditional = diabeticInheritanceSum / (double) diabeticCount;
            if(testInheritance == 0){
                diabeticInheritanceConditional = 1 - diabeticInheritanceConditional;
            }*/
            double diabeticPosteriorNumerator = diabeticPrior * diabeticAgeConditional * diabeticWeightConditional * diabeticGenderConditional * diabeticInheritanceConditional;
        System.out.println("Diabetic: " + diabeticPrior + " " + diabeticAgeConditional + " " + diabeticWeightConditional + " " + diabeticGenderConditional + " " + diabeticInheritanceConditional);
//            System.out.println("P(diabetic): " + diabeticPosteriorNumerator);


         /*   // Test for pre diabetic

            double preDiabeticAgeConditional = calculateConditionalProbability(testAge, preDiabeticAgeMean, preDiabeticAgeVariance);
            double preDiabeticWeightConditional = calculateConditionalProbability(testWeight, preDiabeticWeightMean, preDiabeticWeightVariance);
            double preDiabeticGenderConditional = preDiabeticGenderSum / (double) preDiabeticCount;
            if(testGender == 0){
                preDiabeticGenderConditional = 1 - preDiabeticGenderConditional;
            }
            double preDiabeticInheritanceConditional = preDiabeticInheritanceSum / (double) preDiabeticCount;
            if(testInheritance == 0){
                preDiabeticInheritanceConditional = 1 - preDiabeticInheritanceConditional;
            }
            double preDiabeticPosteriorNumerator = preDiabeticPrior * preDiabeticAgeConditional * preDiabeticWeightConditional * preDiabeticGenderConditional * preDiabeticInheritanceConditional;
            System.out.println("pre diabetic: " + preDiabeticPrior + " " + preDiabeticAgeConditional + " " + preDiabeticWeightConditional + " " + preDiabeticGenderConditional + " " + preDiabeticInheritanceConditional);
//            System.out.println("P(pre diabetic): " + preDiabeticPosteriorNumerator);
*/

            // Test for non diabetic

            double nonDiabeticAgeConditional = calculateConditionalProbability(testAge, nonDiabeticAgeMean, nonDiabeticAgeVariance);
            double nonDiabeticWeightConditional = calculateConditionalProbability(testWeight, nonDiabeticWeightMean, nonDiabeticWeightVariance);
            double nonDiabeticGenderConditional = calculateConditionalProbability(testGender, nonDiabeticGenderMean, nonDiabeticGenderVariance);
            double nonDiabeticInheritanceConditional = calculateConditionalProbability(testInheritance, nonDiabeticInheritanceMean, nonDiabeticInheritanceVariance);/*
            double nonDiabeticGenderConditional = nonDiabeticGenderSum / (double) nonDiabeticCount;
            if(testGender == 0){
                nonDiabeticGenderConditional = 1 - nonDiabeticGenderConditional;
            }
            double nonDiabeticInheritanceConditional = nonDiabeticInheritanceSum / (double) nonDiabeticCount;
            if(testInheritance == 0){
                nonDiabeticInheritanceConditional = 1 - nonDiabeticInheritanceConditional;
            }*/
            double nonDiabeticPosteriorNumerator = nonDiabeticPrior * nonDiabeticAgeConditional * nonDiabeticWeightConditional * nonDiabeticGenderConditional * nonDiabeticInheritanceConditional;
            System.out.println("non diabetic: " + nonDiabeticPrior + " " + nonDiabeticAgeConditional + " " + nonDiabeticWeightConditional + " " + nonDiabeticGenderConditional + " " + nonDiabeticInheritanceConditional);
//            System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator);


            double probabilitySum = diabeticPosteriorNumerator + nonDiabeticPosteriorNumerator;
            System.out.println("P(diabetic): " + diabeticPosteriorNumerator / probabilitySum);
//            System.out.println("P(pre diabetic): " + preDiabeticPosteriorNumerator / probabilitySum);
            System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator / probabilitySum);

            System.out.println("Diagnosis (real) result: " + testClass);
            String testResult;

            if(nonDiabeticPosteriorNumerator >= diabeticPosteriorNumerator){
                testResult = "0";
                error = (diabeticPosteriorNumerator / probabilitySum);
            }
            else{
                testResult = "1";
                error = (nonDiabeticPosteriorNumerator / probabilitySum);
            }
            errorSum += error;

            System.out.println("Test Result: " + testResult);
//            if(testResult.equals("0") && )
            if(testResult.equalsIgnoreCase(testClass)){
                System.out.println("Prediction: Correct!");
                correctCount++;
            }
            else{
                System.out.println("Prediction: Incorrect!");
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Total test case: " + totalCount);
        System.out.println("Correct assumptions: " + correctCount);
        System.out.println("Error: " + (errorSum / totalCount));
//        System.out.println("Accuracy: " + (1 - (errorSum / totalCount)));
//        System.out.println("Accuracy of system: " + correctCount / (double) totalCount);
    }
}