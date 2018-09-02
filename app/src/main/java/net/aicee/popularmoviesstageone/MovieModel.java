package net.aicee.popularmoviesstageone;

public class MovieModel {

        private String id;
        private String voteAverage;
        private String title;
        private String posterPath;

        private String backdropPath;
        private String overview;
        private String releaseDate;

        final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
        final static String POSTER_IMAGE_PATH = "w342";
        final static String BACKDROP_IMAGE_PATH = "w780";





        public MovieModel(String movie_id,
                          String vote_average,
                          String movie_title,
                          String poster_path,
                          String backdrop_path,
                          String movie_overview,
                          String release_date){


            this.id = movie_id;
            this.voteAverage = vote_average;
            this.title = movie_title;
            this.posterPath = poster_path;


            this.backdropPath = backdrop_path;
            this.overview = movie_overview;
            this.releaseDate = release_date;

        }


        public String getMovieId(){
            return id;
        }


        public String getVoteAverage(){
            return voteAverage;
        }

        public void setTitle(String movie_title){
            this.title = movie_title;
        }
        public String getTitle(){
            return title;
        }

        public String getPosterPath(){
            return BASE_IMAGE_URL + POSTER_IMAGE_PATH + posterPath;
        }

        public String getBackdropPath(){
            return BASE_IMAGE_URL + BACKDROP_IMAGE_PATH + backdropPath;
        }


        public String getOverview(){
            return overview;
        }


        public String getReleaseDate(){

            return releaseDate;
        }
    }