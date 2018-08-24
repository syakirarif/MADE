package id.amoled.mademovie.tools;

import org.json.JSONObject;

public class MovieItems {

    private String title, poster_path, overview, release_date, vote_average, backdrop;

    MovieItems(JSONObject object){

        try{
            String title = object.getString("title");
            String poster_path = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String vote_avg = object.getString("vote_average");
            String backdrop = object.getString("backdrop_path");

            this.title = title;
            this.poster_path = poster_path;
            this.overview = overview;
            this.release_date = release_date;
            this.vote_average = vote_avg;
            this.backdrop = backdrop;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getBackdrop(){
        return backdrop;
    }
}
