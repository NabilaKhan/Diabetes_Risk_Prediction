package GaussianNaiveBayes;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mmuaz on 12/22/17.
 */
public class MainBSASNaiveBayesUserInput {
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

    public static double square(double a){
        return a * a;
    }

    public static double updateAvg(double avg, double n, double num){
        return (avg * (n - 1) + num) / n;
    }

    public static void main(String[] args) throws Exception{
        ArrayList ageList = new ArrayList();
        ArrayList weightList = new ArrayList();
        ArrayList genderList = new ArrayList();
        ArrayList inheritanceList = new ArrayList();
        ArrayList classList = new ArrayList();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter age:");
        double testAge = input.nextDouble();
        System.out.println("Enter weight:");
        double testWeight = input.nextInt();
        System.out.println("Enter Gender: (0 if male, 1 if female)");
        int testGender = input.nextInt();
        System.out.println("Enter Inheritance factor: (1 if any of your parents has diabetes, 0 otherwise)");
        int testInheritance = input.nextInt();
        System.out.println("Insert the training data:");
        input.nextLine();
        input.nextLine();

        while(input.hasNextLine()){
            String temp = input.nextLine();
//            System.out.println(temp);
            String store[] = temp.split(",");
//            if(!store[5].equals("")){ //If the sugar level is known
//                System.out.println("bingo");
            double age = Double.valueOf(store[3]);
            double weight = Double.valueOf(store[4]);
            int gender = calculateGenderFactor(store[2]);
            int inheritance = calculateInheritanceFactor(store[6]);
            String className = store[8];
            ageList.add(age);
            weightList.add(weight);
            genderList.add(gender);
            inheritanceList.add(inheritance);
            classList.add(className);
        }
        ageList.add(testAge);
        weightList.add(testWeight);
        genderList.add(testGender);
        inheritanceList.add(testInheritance);
        int totalCount = classList.size();

        int correctCount = 0;
        double errorSum = 0.0;
//        for(int i = 0; i < totalCount; i++){ // leaving one out at each iteration
        //Clustering parameters
        int q = 5; // Maximum number of clusters
        double theta = 17; // Maximum distance to be in same cluster;
        int m = 1; // Initialize the number of clusters

        int testCase = totalCount;
        ArrayList[] clusters = new ArrayList[10];
        double[] clusterAgeAvg = new double[10];
        double[] clusterWeightAvg = new double[10];
        double[] clusterGenderAvg = new double[10];
        double[] clusterInheritanceAvg = new double[10];
        for(int j = 0; j < 10; j++){
            clusters[j] = new ArrayList();
            clusterAgeAvg[j] = 0;
            clusterWeightAvg[j] = 0;
            clusterGenderAvg[j] = 0;
            clusterInheritanceAvg[j] = 0;
        }

        int testCaseCluster = -1;
        clusters[0].add(0);
        clusterAgeAvg[0] = (double)ageList.get(0);
        clusterWeightAvg[0] = (double)weightList.get(0);
        clusterGenderAvg[0] = Double.valueOf(genderList.get(0) + "");
        clusterInheritanceAvg[0] = Double.valueOf(inheritanceList.get(0) + "");
        if(testCase == 0){
            testCaseCluster = 0;
        }
        for(int it = 1; it < totalCount + 1; it++){
                /*if(it != testCase){
                }*/
            double minDistance = 9999999.0;
            int minIndex = -1;
            double age = (double)ageList.get(it);
            double weight = (double)weightList.get(it);
            double gender = Double.valueOf(genderList.get(it) + "");
            double inheritance = Double.valueOf(inheritanceList.get(it) + "");
            for(int it1 = 0; it1 < m; it1++){
                double distance = Math.sqrt(square(age - clusterAgeAvg[it1]) + square(weight - clusterWeightAvg[it1]) + square(gender - clusterGenderAvg[it1]) + square(inheritance - clusterInheritanceAvg[it1]));
                if(distance < minDistance){
                    minDistance = distance;
                    minIndex = it1;
                }
            }
//                System.out.println(minDistance);
            if(minDistance > theta && m < q){ // create a new cluster
                clusters[m].add(it);
                clusterAgeAvg[m] = (double)ageList.get(it);
                clusterWeightAvg[m] = (double)weightList.get(it);
                clusterGenderAvg[m] = Double.valueOf(genderList.get(it) + "");
                clusterInheritanceAvg[m] = Double.valueOf(inheritanceList.get(it) + "");
                if(it == testCase){
                    testCaseCluster = m;
                }
                m++;
            }
            else{
                clusters[minIndex].add(it);
                int clusterSize = clusters[minIndex].size();
                clusterAgeAvg[minIndex] = updateAvg (clusterAgeAvg[minIndex], clusterSize, (double)ageList.get(it));
                clusterWeightAvg[minIndex] = updateAvg (clusterWeightAvg[minIndex], clusterSize, (double)weightList.get(it));
                clusterGenderAvg[minIndex] = updateAvg (clusterGenderAvg[minIndex], clusterSize, Double.valueOf(genderList.get(it) + ""));
                clusterInheritanceAvg[minIndex] = updateAvg (clusterInheritanceAvg[minIndex], clusterSize, Double.valueOf(inheritanceList.get(it) + ""));
                if(it == testCase){
                    testCaseCluster = minIndex;
                }
            }
        }

        for(int it = 0; it < m; it++){
            System.out.println(clusters[it]);
        }
        System.out.println(testCaseCluster);

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

        for(int it = 0; it < clusters[testCaseCluster].size(); it++){
            int curIndex = Integer.valueOf(clusters[testCaseCluster].get(it).toString());
            if(curIndex == testCase){
                continue;
            }
            double age = Double.valueOf(ageList.get(curIndex).toString());
            double weight = Double.valueOf(weightList.get(curIndex).toString());
            int gender = Integer.valueOf(genderList.get(curIndex).toString());
            int inheritance = Integer.valueOf(inheritanceList.get(curIndex).toString());
            if(classList.get(curIndex).toString().equalsIgnoreCase("non")){
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
            else if(classList.get(curIndex).toString().equalsIgnoreCase("pre")){
                preDiabeticCount++;
                preDiabeticAgeSum += age;
                preDiabeticAgeSqSum += (age * age);
                preDiabeticWeightSum += weight;
                preDiabeticWeightSqSum += (weight * weight);
                preDiabeticGenderSum += gender;
                preDiabeticGenderSqSum += (gender * gender);
                preDiabeticInheritanceSum += inheritance;
                preDiabeticInheritanceSqSum += (inheritance * inheritance);
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

        int totalCountInCluster = diabeticCount + preDiabeticCount + nonDiabeticCount;


        double diabeticPrior = (double) diabeticCount / (double) totalCountInCluster;
        double preDiabeticPrior = (double) preDiabeticCount / (double) totalCountInCluster;
        double nonDiabeticPrior = (double) nonDiabeticCount / (double) totalCountInCluster;

        double error = 0;

        System.out.println("Test #" + (testCase + 1) + ":");
        System.out.println();
        System.out.println("Age: " + testAge + ",  Weight: " + testWeight + ", Gender: " + testGender + ", Inheritance: " + testInheritance);

        // Test for diabetic
        double diabeticAgeConditional = calculateConditionalProbability(testAge, diabeticAgeMean, diabeticAgeVariance);
        double diabeticWeightConditional = calculateConditionalProbability(testWeight, diabeticWeightMean, diabeticWeightVariance);
        double diabeticGenderConditional = diabeticGenderSum / (double) diabeticCount;
        if(testGender == 0){
            diabeticGenderConditional = 1 - diabeticGenderConditional;
        }
        double diabeticInheritanceConditional = diabeticInheritanceSum / (double) diabeticCount;
        if(testInheritance == 0){
            diabeticInheritanceConditional = 1 - diabeticInheritanceConditional;
        }
        double diabeticPosteriorNumerator = diabeticPrior * diabeticAgeConditional * diabeticWeightConditional * diabeticGenderConditional * diabeticInheritanceConditional;
        System.out.println("Diabetic: " + diabeticPrior + " " + diabeticAgeConditional + " " + diabeticWeightConditional + " " + diabeticGenderConditional + " " + diabeticInheritanceConditional);
//            System.out.println("P(diabetic): " + diabeticPosteriorNumerator);

        // Test for pre diabetic

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


        // Test for non diabetic

        double nonDiabeticAgeConditional = calculateConditionalProbability(testAge, nonDiabeticAgeMean, nonDiabeticAgeVariance);
        double nonDiabeticWeightConditional = calculateConditionalProbability(testWeight, nonDiabeticWeightMean, nonDiabeticWeightVariance);
        double nonDiabeticGenderConditional = nonDiabeticGenderSum / (double) nonDiabeticCount;
        if(testGender == 0){
            nonDiabeticGenderConditional = 1 - nonDiabeticGenderConditional;
        }
        double nonDiabeticInheritanceConditional = nonDiabeticInheritanceSum / (double) nonDiabeticCount;
        if(testInheritance == 0){
            nonDiabeticInheritanceConditional = 1 - nonDiabeticInheritanceConditional;
        }
        double nonDiabeticPosteriorNumerator = nonDiabeticPrior * nonDiabeticAgeConditional * nonDiabeticWeightConditional * nonDiabeticGenderConditional * nonDiabeticInheritanceConditional;
        System.out.println("non diabetic: " + nonDiabeticPrior + " " + nonDiabeticAgeConditional + " " + nonDiabeticWeightConditional + " " + nonDiabeticGenderConditional + " " + nonDiabeticInheritanceConditional);
//            System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator);

        double probabilitySum = diabeticPosteriorNumerator + preDiabeticPosteriorNumerator + nonDiabeticPosteriorNumerator;
        System.out.println("P(diabetic): " + diabeticPosteriorNumerator / probabilitySum);
        System.out.println("P(pre diabetic): " + preDiabeticPosteriorNumerator / probabilitySum);
        System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator / probabilitySum);

//        System.out.println("Diagnosis (real) result: " + testClass);
        String testResult;
        if(nonDiabeticPosteriorNumerator >= diabeticPosteriorNumerator && nonDiabeticPosteriorNumerator >= preDiabeticPosteriorNumerator){
            testResult = "non";
//            error = (diabeticPosteriorNumerator / probabilitySum) + (preDiabeticPosteriorNumerator / probabilitySum);
        }
        else if(preDiabeticPosteriorNumerator >= diabeticPosteriorNumerator && preDiabeticPosteriorNumerator >= nonDiabeticPosteriorNumerator){
            testResult = "pre";
//            error = (diabeticPosteriorNumerator / probabilitySum) + (nonDiabeticPosteriorNumerator / probabilitySum);
        }
        else{
            testResult = "diabetic";
//            error = (nonDiabeticPosteriorNumerator / probabilitySum) + (preDiabeticPosteriorNumerator / probabilitySum);
        }
        errorSum += error;

        System.out.println("Test Result: " + testResult);
        /*if(testResult.equalsIgnoreCase(testClass)){
            System.out.println("Prediction: Correct!");
            correctCount++;
        }
        else{
            System.out.println("Prediction: Incorrect!");
        }*/
//        }

//        System.out.println("Accuracy: " + (1 - (errorSum / totalCount)));
//        System.out.println("Accuracy of system: " + correctCount / (double) totalCount);
    }
}
