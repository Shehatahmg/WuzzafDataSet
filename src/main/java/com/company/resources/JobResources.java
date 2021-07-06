/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import smile.data.DataFrame;
import smile.data.vector.IntVector;

/**
 *
 * @author MIDO
 */
@Path("/")
public class JobResources {
//    @GET
//    @Path("/JobResources")
//    @Produces(MediaType.TEXT_HTML)
//    public String index() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("<br><a href=http://localhost:8080/api/summary>Print Data Summary</a> ==> " +
//                "<b>http://localhost:8080/api/summary</b>");
//        builder.append("<br><a href=http://localhost:8080/api/skills>Print Skills</a> ==> " +
//                "<b>http://localhost:8080/api/skills</b>");
//        builder.append("<br><a href=http://localhost:8080/api/companies>Print Companies</a> ==> " +
//                "<b>http://localhost:8080/api/companies</b>");
//        builder.append("<br><a href=http://localhost:8080/api/jobs>Print Job Titles</a> ==> " +
//                "<b>http://localhost:8080/api/jobs</b>");
//        builder.append("<br><a href=http://localhost:8080/api/locations>Print Locations</a> ==> " +
//                "<b>http://localhost:8080/api/locations</b>");
//        builder.append("<br><a href=http://localhost:8080/api/jobBar>Print Job BarChart</a> ==> " +
//                "<b>http://localhost:8080/api/jobBar</b>");
//        builder.append("<br><a href=http://localhost:8080/api/areaBar>Print Area BarChart</a> ==> " +
//                "<b>http://localhost:8080/api/areaBar</b>");
//        builder.append("<br><a href=http://localhost:8080/api/companyPie>Print Company PieChart</a> ==> " +
//                "<b>http://localhost:8080/api/companyPie</b>");
//        return builder.toString();
//    }
    
    //
   JobDao x = new JobDao();
   @GET 
   @Path("/data") 
   @Produces(MediaType.APPLICATION_JSON)
   public ArrayList<Job> getSomeData(){
       return Methods.countTenJobs((ArrayList<Job>) x.c_list);
   }
    // Creating dummy object to call functions from the Methods Class
    Methods obj = new Methods();
    //Using SMILE to print summary for the data
    DataFrame jobSM = obj.readCSV ("src/main/resources/Wuzzuf_Jobs.csv");
   @GET 
   @Path("/summary") 
   @Produces(MediaType.APPLICATION_JSON) 
   public LinkedHashMap<String, String> getSummary(){ 
        LinkedHashMap<String, String> summary = new LinkedHashMap<String, String>();
      summary.put("Number of Rows ",String.valueOf(jobSM.vector("YearsExp").size()));
      summary.put("Columns","Type");
      for(int i = 0;i<jobSM.ncols();i++){
         summary.put(jobSM.column(i).name(),jobSM.column(i).type().toString());
      }
      return summary;
     
   }
   
   // Enocoding YearsExp column
    DataFrame vectorizedJobSM = jobSM.merge (IntVector.of ("YearsExpValues",
            Methods.encodeCategory (jobSM, "YearsExp")));
    
   //Create a Spark conext
    SparkConf conf = new SparkConf().setAppName("Jobs").setMaster("local[3]");
    JavaSparkContext context= new JavaSparkContext(conf);
   // LOAD DATASETS
    JavaRDD<String> WuzzufDataSet= context.textFile("src/main/resources/Wuzzuf_Jobs.csv");
    //Removing Nulls 
    DataFrame newJobSM = Methods.processData(jobSM);
    
    //Transformation and removing duplicates
    JavaRDD<String> WuzzufDataSetUpdated= WuzzufDataSet.distinct();
    //Transformation
    JavaRDD<String> jobs= WuzzufDataSetUpdated
            .map(Methods::extractjobs)
            .filter(StringUtils::isNotBlank);


    JavaRDD<String> company= WuzzufDataSetUpdated
            .map(Methods::extractcompanies)
            .filter(StringUtils::isNotBlank);

    JavaRDD<String> location= WuzzufDataSetUpdated
            .map(Methods::extractlocation)
            .filter(StringUtils::isNotBlank);

    JavaRDD<String> skills= WuzzufDataSetUpdated
            .map(Methods::extractskills)
            .filter(StringUtils::isNotBlank);
    Map<String, Long> sortedSkills = obj.countSkills(skills);
    @GET 
    @Path("/skills") 
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedHashMap<String, Long> getSkillsCount(){
        return obj.countTenValues(sortedSkills);
    }
    Map<String, Long> sortedCompany = Methods.countRows(company);
    @GET 
    @Path("/companies") 
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedHashMap<String, Long> getCompanyCount(){
        return obj.countTenValues(sortedCompany);
    }
    Map<String, Long> sortedJobs= Methods.countRows(jobs);
    @GET 
    @Path("/jobs") 
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedHashMap<String, Long> getJobsCount(){
        return obj.countTenValues(sortedJobs);
    }
    Map<String, Long> sortedLocation= Methods.countRows(location);
    @GET 
    @Path("/locations") 
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedHashMap<String, Long> getLocationsCount(){
        return obj.countTenValues(sortedJobs);
    }
    ArrayList<String> jobKeys= new ArrayList<String>(sortedJobs.keySet());
    ArrayList<Long> jobValues= new ArrayList<Long>(sortedJobs.values());
    
    ArrayList<String> locationKeys= new ArrayList<String>(sortedLocation.keySet());
    ArrayList<Long> locationValues= new ArrayList<Long>(sortedLocation.values());
    

    ArrayList<String> first8JobKeys= (ArrayList<String>) jobKeys.stream().limit(8).collect(Collectors.toList());
    ArrayList<Long> first8JobValues= (ArrayList<Long>) jobValues.stream().limit(8).collect(Collectors.toList());
    
    ArrayList<String> first8LocationKeys= (ArrayList<String>) locationKeys.stream().limit(8).collect(Collectors.toList());
    ArrayList<Long> first8LocationValues= (ArrayList<Long>) locationValues.stream().limit(8).collect(Collectors.toList());
                
    //Calling graphJobPopularity to plot the bar chart
    CategoryChart jobChart = Methods.graphJobPopularity(first8JobKeys, first8JobValues);
    @GET 
    @Path("/jobBar") 
    @Produces(MediaType.TEXT_HTML)
    public String sendJobChartString() throws IOException {
        String bytes = null;
        byte[] image_bytes = BitmapEncoder.getBitmapBytes(jobChart , BitmapEncoder.BitmapFormat.PNG);
        bytes = Base64.getEncoder().encodeToString(image_bytes);
        return "<img src=\"data:image/png;base64,"+bytes+"\" />";
    }
    CategoryChart areaChart = Methods.graphPopularAreas(first8LocationKeys, first8LocationValues);
    @GET 
    @Path("/areaBar") 
    @Produces(MediaType.TEXT_HTML)
    public String sendAreaChartString() throws IOException {
        String bytes = null;
        byte[] image_bytes = BitmapEncoder.getBitmapBytes(areaChart , BitmapEncoder.BitmapFormat.PNG);
        bytes = Base64.getEncoder().encodeToString(image_bytes);
        return "<img src=\"data:image/png;base64,"+bytes+"\" />";
    }
    PieChart companyChart = Methods.pieChart(sortedCompany);
    @GET 
    @Path("/companyPie") 
    @Produces(MediaType.TEXT_HTML)
    public String sendCompanyChartString() throws IOException {
        String bytes = null;
        byte[] image_bytes = BitmapEncoder.getBitmapBytes(companyChart , BitmapEncoder.BitmapFormat.PNG);
        bytes = Base64.getEncoder().encodeToString(image_bytes);
        return "<img src=\"data:image/png;base64,"+bytes+"\" />";
    }
}
    