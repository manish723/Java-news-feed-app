/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author manish
 */
@WebServlet(name = "NewServlet", urlPatterns = {"/"})
public class NewServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Collections collections;
    Connection con = null;
    Statement st = null;
    ResultSet rset = null;
    public MongoClient client;
    public DBCollection coll;
 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            FeedFetcher fetcher = new HttpURLFeedFetcher();
            client = new MongoClient("localhost", 27017);
            coll = client.getDB("NewsDB").getCollection("News-Feed");
            //Getting the collection and storing the collection in MongoDB
            SyndFeed feed = fetcher.retrieveFeed(new URL("http://www.thehindubusinessline.com/?service=rss"));
            for (Object obj : feed.getEntries()) {
                SyndEntry entry = (SyndEntry) obj;
                //Creating a DB object(or document)
                BasicDBObject data = new BasicDBObject();
                //Appending the data to the DB object with a key-value pair insertion.
                data.append("NewsId", entry.getTitle());
                //Adding the document to the collection.
                coll.insert(data);
                //   response.getWriter().println(entry.getTitle());  
            }

            //Displaying the data in the JSON format     
            
            //Creating a cursor again to get the while data from existing collecting
            DBCursor cursor = coll.find();   
            JSON json = new JSON();
            //Converting the data into a JSON string
            String serialize = json.serialize(cursor);
            System.out.println(serialize);

            response.getWriter().println("Document inserted successfully");

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FeedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FetcherException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public DBCollection getCollecion() {
        return client.getDB("NewsDB").getCollection("News-Feed");
    }

}
