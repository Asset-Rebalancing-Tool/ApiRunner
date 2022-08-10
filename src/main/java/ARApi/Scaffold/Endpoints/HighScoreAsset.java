package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.PublicAsset;

public class HighScoreAsset {

    public final PublicAsset publicAsset;

    public final int highestFuzzyScore;

    public HighScoreAsset(PublicAsset publicAsset, int highestFuzzyScore) {
        this.publicAsset = publicAsset;
        this.highestFuzzyScore = highestFuzzyScore;
    }
}
