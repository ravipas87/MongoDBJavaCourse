package course;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // XXX HW 3.2,  Work Here
        Document post = null;
        //ini ravipas
        Bson filter=eq("permalink",permalink);
        post=postsCollection.find(filter).first(); //there will be just one
        //end ravipas

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        List<Document> posts = null;
        Bson sort=descending("date");
        
        //INI RAVIPAS
        posts=postsCollection.find().sort(sort).limit(limit).into(new ArrayList<Document>());
        //END RAVIPAS
        
        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date, title
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document();
        //INI RAVIPAS
        post.append("permalink", permalink);
        post.append("title", title);
        post.append("author", username);
        post.append("body", body);
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
        Date date=new Date();
        post.append("date",dateFormat.format(date));
        post.append("tags", tags);
        //list of comments
        List comments= new ArrayList();
        post.append("comments", comments);
        postsCollection.insertOne(post); //insert in DB
        //END RAVIPAS
        
        return permalink;
    }




    // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments
    	
    	//INI RAVIPAS
    	
    	Document comment=new Document();
    	comment.append("author", name);
    	comment.append("body", body);
    	
    	//optional mail
    	if(email!=null) comment.append("email", email);
    	//find and update
    	postsCollection.findOneAndUpdate(eq("permalink",permalink), Updates.addToSet("comments", comment));
    	
    	//END RAVIPAS
    	
    }
}
