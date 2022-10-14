package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;

import java.util.List;

public class MatchingResult {

    public PublicAsset exactMatch;

    public List<PublicAsset> looseMatches;
}
