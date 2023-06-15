package com.example.ifixit.SERVICE_PROVIDER_FILES.Messaging.ChatListThreads;

public class ChatListItem {

    public String chatKey;
    public String name;
    public String service;
    public String profileimagurl;


    public ChatListItem(String chatKey, String name, String service, String profileimagurl) {
        this.chatKey = chatKey;
        this.name = name;
        this.service = service;
        this.profileimagurl = profileimagurl;
    }

    public String getChatKey() {
        return chatKey;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public String getProfileimagurl() {
        return profileimagurl;
    }
}
