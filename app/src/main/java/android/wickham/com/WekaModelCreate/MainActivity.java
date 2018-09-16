package android.wickham.com.WekaModelCreate;

/*
 * Copyright (C) 2018 Mark Wickham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.KStar;
import weka.classifiers.trees.RandomForest;
import weka.clusterers.Clusterer;
import weka.clusterers.EM;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataSource sourceTrain, sourceTest = null;

        try {
            // Load the Training data
            sourceTrain = new DataSource(getResources().openRawResource(R.raw.oldfaithful_train));
            Instances dataTrain = sourceTrain.getDataSet();

            // Load the Test data
            sourceTest = new DataSource(getResources().openRawResource(R.raw.oldfaithful_test));
            Instances dataTest = sourceTest.getDataSet();

            // Set the class attribute (Label) as the last class
            if (dataTrain.classIndex() == -1) dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
            if (dataTest.classIndex() == -1) dataTest.setClassIndex(dataTest.numAttributes() - 1);

            // Fill the summary information for the dataTrain set
            int attrs = dataTrain.numAttributes();
            int classes = dataTrain.numClasses();
            int insts = dataTrain.numInstances();

            // Setup a Random Forest classifier
            Classifier rf = new RandomForest();

            // Other classifiers or clusterers can be defined as follows
            Classifier nb = new NaiveBayes();
            Classifier knn = new KStar();
            Classifier svm = new SMO();
            Clusterer EM = new EM();
            Clusterer KMeans = new SimpleKMeans();
            Clusterer DBSCAN = new MakeDensityBasedClusterer();

            // Train the RF classifier
            rf.buildClassifier(dataTrain);

            // Evaluate the classifier and print the results
            Evaluation eval = new Evaluation(dataTest);
            eval.evaluateModel(rf, dataTest);

            // Show the results
            TextView tv_attrs = (TextView) findViewById(R.id.attrs);
            tv_attrs.setText(String.valueOf(attrs));
            TextView tv_classes = (TextView) findViewById(R.id.classes);
            tv_classes.setText(String.valueOf(classes));
            TextView tv_insts = (TextView) findViewById(R.id.insts);
            tv_insts.setText(String.valueOf(insts));
            TextView results = (TextView) findViewById(R.id.results);
            results.setText((CharSequence) eval.toSummaryString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
