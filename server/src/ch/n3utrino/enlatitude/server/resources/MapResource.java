package ch.n3utrino.enlatitude.server.resources;

import ch.n3utrino.enlatitude.common.UpdateReply;
import ch.n3utrino.enlatitude.common.UpdateRequest;
import ch.n3utrino.enlatitude.common.User;
import com.google.gson.Gson;


import javax.ws.rs.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Produces("application/json")
@Path("/")
public class MapResource {


    private Gson gson = new Gson();
    private DataAccessor dataAccessor = DataAccessor.getInstance();

    @POST
    @Path("/update/")
    public String update(String updateRequest) {

        UpdateRequest requestObject = gson.fromJson(updateRequest, UpdateRequest.class);
        User theUser =  requestObject.getUser();
        theUser.setLastUpdate(System.currentTimeMillis());
        dataAccessor.updateUser(theUser);


        UpdateReply reply = new UpdateReply();
        reply.setStatus("OK");

        Map<String,User> replyMap = new HashMap<String, User>();
        Set<User> removeMap = new HashSet<User>();

        for(User user:dataAccessor.getUsers().values()){
            if(System.currentTimeMillis() - user.getLastUpdate() < 30*60*1000){
                replyMap.put(user.getUuid(),user);
            } else {
               removeMap.add(user);
            }
        }

        dataAccessor.removeUsers(removeMap);

        reply.setUsers(replyMap);

        return gson.toJson(reply);

    }


}
