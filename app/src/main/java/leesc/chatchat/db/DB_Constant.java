package leesc.chatchat.db;

public interface DB_Constant {
    public static final int QUERY_SEND_MESSAGE = 0x01;
    public static final int QUERY_RECEIVED_MESSAGE = 0x02;
    public static final int QUERY_MESSAGE = 0x03;
    public static final int QUERY_SORT_DESC = 0x04;
    public static final int QUERY_SORT_ASC = 0x05;
    public static final int ADD = 0;
    public static final int REMOVE = 1;
    
    // Message DB
    public static String TABLE_NAME = "chat_chat";
    public static String FIELD_DATE = "date";
    public static String FIELD_BODY = "body";
    public static String FILED_MSG_TYPE = "message_type";
    public static String FIELD_TYPE = "type";
    public static String FIELD_ADDRESS = "address";
    public static String FIELD_STATUS = "status";
    public static String FIELD_NAME = "name";
    
    // Threads DB
    public static String THREADS_TABLE_NAME = "threads";
    public static String THREADS_FIELD_THREAD_EMAIL = "email";
    public static String THREADS_FIELD_THREAD_NAMES = "names";

    // Conversation DB
    public static String CONVERSATION_TABLE_NAME = "conversation";
    public static String CONVERSATION_FIELD_THREAD_ID = "threadId";
    public static String CONVERSATION_FIELD_SNIPPET = "snippet";
    public static String CONVERSATION_FIELD_DATE = "date";
    public static String CONVERSATION_FIELD_STATUS = "status";
    public static String CONVERSATION_FIELD_MSG_COUNT = "msg_count";
    public static String CONVERSATION_FIELD_UNREAD_COUNT = "unreadcount";
    public static String CONVERSATION_FIELD_FAVORITY = "favority";
    public static String CONVERSATION_FIELD_BLOCKED = "blocked";

    // Message DB
    public static String MESSAGES_TABLE_NAME = "messages";
    public static String MESSAGES_FIELD_ID ="_id";
    public static String MESSAGES_FIELD_THREAD_ID = "threadId";
    public static String MESSAGES_FIELD_EMAIL = "email";
    public static String MESSAGES_FIELD_NAMES = "names";
    public static String MESSAGES_FIELD_BODY = "body";
    public static String MESSAGES_FIELD_DATE = "date";
    public static String MESSAGES_FIELD_TYPE = "message_type";
    public static String MESSAGES_FIELD_STATUS = "status";
    public static String MESSAGES_FIELD_READ = "read";
    
    // Contacts DB
    public static String CONTACTS_TABLE_NAME = "contacts";
    public static String CONTACTS_FIELD_NAME = "name";
    public static String CONTACTS_FIELD_PART = "part";
    public static String CONTACTS_FIELD_ADDRESS = "address";
    public static String CONTACTS_FIELD_TYPE = "type";
    public static String CONTACTS_FIELD_POSITION = "position";

    //Friend DB
    public static String FRIEND_TABLE_NAME ="friend";
    public static String FRIEND_FIELD_FRIEND_ID ="friendId";
    public static String FRIEND_FIELD_FRIEND_EMAIL ="friendEmail";
    public static String FRIEND_FIELD_FRIEND_NAME ="friendName";
    public static String FRIEND_FIELD_FRIEND_TOKEN ="friendToken";
    public static String FRIEND_FIELD_FRIEND_DEVICE ="friendDevice";

    public static final int SEND_TYPE = 0x01;
    public static final int RECEIVE_TYPE = 0x02;

    public static final int SEND_TYPE_FAIL = 0x03;

    public static final int STATUS_OK = 0x01;
    public static final int STATUS_FAIL = 0x02;

}
