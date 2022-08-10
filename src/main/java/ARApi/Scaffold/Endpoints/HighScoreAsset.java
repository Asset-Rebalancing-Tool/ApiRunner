package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.PublicAsset;

/**
 * Result container after using {@link ARApi.Scaffold.Services.StringProcessingService} to calculate scores for different fields.
 */
public class HighScoreAsset {

    public final PublicAsset publicAsset;

    public final int highestFuzzyScore;

    public HighScoreAsset(PublicAsset publicAsset, int highestFuzzyScore) {
        this.publicAsset = publicAsset;
        this.highestFuzzyScore = highestFuzzyScore;
    }
}
