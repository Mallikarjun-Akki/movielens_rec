package com.maldyapps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.cli2.OptionException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;

public class MovielensRecommender {
	public static void main(String[] args) throws FileNotFoundException, TasteException, IOException, OptionException {
		DataModel model;
		model = new FileDataModel(new File("datasets/ratingsForMahout.dat"));
		
		File movieMapFile = new File("datasets/moviesForMahout.dat");
		HashMap<Long, String> movieMap = new HashMap<Long, String>();
		Scanner scan = new Scanner(movieMapFile);
		while(scan.hasNextLine()){
			String[] line = scan.nextLine().split("\\|");
			movieMap.put(Long.parseLong(line[0]), line[1]);
		}
		scan.close();

		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
		Recommender cachingRecommender = new CachingRecommender(recommender);
		
		for (LongPrimitiveIterator it = model.getUserIDs(); it.hasNext();){
            long userId = it.nextLong();
            List<RecommendedItem> recommendations = cachingRecommender.recommend(userId, 10);
            if (recommendations.size() == 0){
            	System.out.println("User " + userId + ": no recommendations");
            }
            for (RecommendedItem recommendedItem : recommendations) {
            	System.out.println("User " + userId + ": " + movieMap.get(recommendedItem.getItemID()) + "; value=" + recommendedItem.getValue());
            }
		}
	}
}