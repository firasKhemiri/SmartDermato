package smartdermato.esprit.tn.smartdermato.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Entities.Users;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "smartdermato.db";

    private static final String TABLE_CONTACT_MEDECIN = "Contact_Medecin";

    private static final String COL1="FIRST_NAME";
    private static final String COL2="LAST_NAME";
    private static final String COL3="IMAGE_MEDECIN";
    private static final String COL4="OFFICE_PHONE";
    private static final String COL5="OFFICE_ADDRESS";
    private static final String COL6="POSTAL_CODE";
    private static final String COL7="CITY";
    private static final String COL8="COUNTRY";
    private static final String COL9="EMAIL";
    private static final String COL10="IDM";
    private static final String COL11="STATUS";
    private static final String COL12="CODE_OFFICE_PHONE";



    SQLiteDatabase db;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null,1);
         db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE "+TABLE_CONTACT_MEDECIN+"(CONTACT_MEDECIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COL1+" TEXT,"+COL2+" TEXT,"+COL3+" TEXT,"+COL4+" TEXT,"+COL5+" TEXT,"+COL6+" TEXT,"+COL7+" TEXT,"+COL8+" TEXT,"+COL9+" TEXT,"+COL10+" INTEGER,"+COL11+" TEXT,"+COL12+" TEXT);";
        System.out.println("Requet:  "+createTableUsers);

        db.execSQL(createTableUsers);

        System.out.println(db+"NOM++++++++++++");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACT_MEDECIN);


          onCreate(db);
        System.out.println(db+"NOM////////////////////////////");

        db.close();
    }
    public boolean addContactMedecin(String FIRST_NAME,String LAST_NAME,String IMAGE_MEDECIN,String OFFICE_PHONE,String OFFICE_ADDRESS,String POSTAL_CODE,String CITY,String COUNTRY,String EMAIL,int IDM,String STATUS,String CODE_OFFICE_PHONE){

        ContentValues contactMedecin = new ContentValues();
        contactMedecin.put(COL1,FIRST_NAME);
        contactMedecin.put(COL2,LAST_NAME);
        contactMedecin.put(COL3,IMAGE_MEDECIN);
        contactMedecin.put(COL4,OFFICE_PHONE);
        contactMedecin.put(COL5,OFFICE_ADDRESS);
        contactMedecin.put(COL6,POSTAL_CODE);
        contactMedecin.put(COL7,CITY);
        contactMedecin.put(COL8,COUNTRY);
        contactMedecin.put(COL9,EMAIL);
        contactMedecin.put(COL10,IDM);
        contactMedecin.put(COL11,STATUS);
        contactMedecin.put(COL12,CODE_OFFICE_PHONE);
        System.out.println("contactMedecin"+contactMedecin);
        long result = db.insert(TABLE_CONTACT_MEDECIN,null,contactMedecin);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

//    public boolean updateReaction(Reactions reactions){
//        Cursor data = getStatuts(reactions.getStatuss().getId());
//        int idStatuts =0;
//        while (data.moveToNext()){
//            idStatuts = data.getInt(0);
//        }
//        Cursor data2 = getUsers(reactions.getUsers().getUsername());
//        int idUsers = 0 ;
//        while (data2.moveToNext()){
//            idUsers = data2.getInt(0);
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL1US, reactions.getId());
//        contentValues.put(COL2US, idStatuts);
//        contentValues.put(COL3US, idUsers);
//        contentValues.put(COL4US, reactions.getConf_confp());
//        long result2 = db.update(TABLE_REACTIONS,contentValues,"ID_REACTION = "+reactions.getId(),null);
//        if (result2 == -1) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//    public List<Users> getUsers(String motCle)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_CONTACT_MEDECIN+" WHERE "+COL1+" = ? OR "+COL2+" = ? OR "+COL4+" = ? OR "+COL5+" = ? OR "+COL6+" = ? OR "+COL7+" = ? ";
//        Cursor data = db.rawQuery(query,new String[]{motCle,motCle,motCle,motCle,motCle,motCle});
//        List<Users> user = new ArrayList<>();
//        if(data.moveToFirst()){
//            do {
//                Users users = new Users();
//                users.setLastName(data.getString(data.getColumnIndex(COL2)));
//                users.setFirstName(data.getString(data.getColumnIndex(COL1)));
//                users.setUser_pic(data.getString(data.getColumnIndex(COL3)));
//                users.setOfficeAddess(data.getString(data.getColumnIndex(COL5)));
//                users.setOfficeNumber(data.getString(data.getColumnIndex(COL4)));
//                users.setCity(data.getString(data.getColumnIndex(COL7)));
//                users.setCountry(data.getString(data.getColumnIndex(COL8)));
//                users.setPostalCode(data.getString(data.getColumnIndex(COL6)));
//                user.add(users);
//            }while (data.moveToNext());
//        }
//        return user;
//    }
//    public List<Users> getAllUsers(){
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_CONTACT_MEDECIN;
//        Cursor data = db.rawQuery(query,null);
//        List<Users> user = new ArrayList<>();
//        if(data.moveToFirst()){
//            do {
//                Users users = new Users();
//                users.setLastName(data.getString(data.getColumnIndex(COL2)));
//                users.setFirstName(data.getString(data.getColumnIndex(COL1)));
//                users.setUser_pic(data.getString(data.getColumnIndex(COL3)));
//                users.setOfficeAddess(data.getString(data.getColumnIndex(COL5)));
//                users.setOfficeNumber(data.getString(data.getColumnIndex(COL4)));
//                users.setCity(data.getString(data.getColumnIndex(COL7)));
//                users.setCountry(data.getString(data.getColumnIndex(COL8)));
//                users.setPostalCode(data.getString(data.getColumnIndex(COL6)));
//                user.add(users);
//            }while (data.moveToNext());
//        }
//        return user;
//    }
    public List<Users> getAllUsers()
    {
        db = this.getWritableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"CONTACT_MEDECIN_ID",COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12};
        qb.setTables(TABLE_CONTACT_MEDECIN);
        Cursor data = qb.query(db,sqlSelect,null,null,null,null,null);
        List<Users> user = new ArrayList<>();
        if(data.moveToFirst()){
            do {
                Users users = new Users();
                users.setLastName(data.getString(data.getColumnIndex(COL2)));
                users.setFirstName(data.getString(data.getColumnIndex(COL1)));
                users.setUser_pic(data.getString(data.getColumnIndex(COL3)));
                users.setOfficeAddess(data.getString(data.getColumnIndex(COL5)));
                users.setOfficeNumber(data.getString(data.getColumnIndex(COL4)));
                users.setCity(data.getString(data.getColumnIndex(COL7)));
                users.setCountry(data.getString(data.getColumnIndex(COL8)));
                users.setPostalCode(data.getString(data.getColumnIndex(COL6)));
                users.setEmail(data.getString(data.getColumnIndex(COL9)));
                users.setId(data.getInt(data.getColumnIndex(COL10)));
                users.setStatus(data.getString(data.getColumnIndex(COL11)));
                users.setCountryOfficeNumber(data.getString(data.getColumnIndex(COL12)));
            user.add(users);
            }while (data.moveToNext());
        }
        return user;
    }
    public List<String> getName()
    {
        db = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"CONTACT_MEDECIN_ID",COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12};
        qb.setTables(TABLE_CONTACT_MEDECIN);
        Cursor data = qb.query(db,sqlSelect,null,null,null,null,null);
        List<String> user = new ArrayList<>();
        if(data.moveToFirst()){
            do {

                user.add(data.getString(data.getColumnIndex(COL1))+" "+data.getString(data.getColumnIndex(COL2)));
                System.out.println("get name: "+data.getString(data.getColumnIndex(COL1))+" "+data.getString(data.getColumnIndex(COL2)));
            }while (data.moveToNext());
        }
        return user;
    }
    public List<Users> getUserByName (String name){
        System.out.println(name);

        db = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"CONTACT_MEDECIN_ID",COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12};
        qb.setTables(TABLE_CONTACT_MEDECIN);
        Cursor data = qb.query(db,sqlSelect,null,null,null,null,null);
        List<Users> user = new ArrayList<>();
        if(data.moveToFirst()){
            do {

                String nameConc = data.getString(data.getColumnIndex(COL1))+" "+data.getString(data.getColumnIndex(COL2));
                System.out.println("name: "+nameConc);
                if(nameConc.equalsIgnoreCase(name)){
                    Users users = new Users();
                    users.setFirstName(data.getString(data.getColumnIndex(COL1)));
                    users.setLastName(data.getString(data.getColumnIndex(COL2)));
                    users.setUser_pic(data.getString(data.getColumnIndex(COL3)));
                    users.setOfficeAddess(data.getString(data.getColumnIndex(COL5)));
                    users.setOfficeNumber(data.getString(data.getColumnIndex(COL4)));
                    users.setCity(data.getString(data.getColumnIndex(COL7)));
                    users.setCountry(data.getString(data.getColumnIndex(COL8)));
                    users.setPostalCode(data.getString(data.getColumnIndex(COL6)));
                    users.setEmail(data.getString(data.getColumnIndex(COL9)));
                    users.setId(data.getInt(data.getColumnIndex(COL10)));
                    users.setStatus(data.getString(data.getColumnIndex(COL11)));
                    users.setCountryOfficeNumber(data.getString(data.getColumnIndex(COL12)));
                    System.out.println(user);
                    user.add(users);
                }
            }while (data.moveToNext());
        }


        return user;
    }
//    public Cursor getUsersByIDU(int id_user)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_USERS+" WHERE ID_USERS = ? ";
//        Cursor data = db.rawQuery(query,new String[]{String.valueOf(id_user)});
//
//
//        return data;
//    }
//    public Cursor getReactions(int id_reaction)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_REACTIONS+" WHERE ID_REACTION = ?  ";
//        Cursor data = db.rawQuery(query,new String[]{String.valueOf(id_reaction)});
//        return data;
//    }
//    public Cursor getAllUsers()
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_USERS;
//        Cursor data = db.rawQuery(query,null);
//        return data;
//    }
//    public Cursor getUsersByID(int id)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_USERS+" WHERE USER_ID = ? ";
//        Cursor data = db.rawQuery(query,new String[]{String.valueOf(id)});
//        return data;
//    }
//    public Cursor getReactionsByIDS(int id)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_REACTIONS+" WHERE STATUTS_ID = ? ";
//        Cursor data = db.rawQuery(query,new String[]{String.valueOf(id)});
//        return data;
//    }
//    public Cursor getStatuts(int id_Statuts)
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_STATUTS+" WHERE ID_STATUTS = ? ";
//        Cursor data = db.rawQuery(query,new String[]{String.valueOf(id_Statuts)});
//        return data;
//    }
//
//    public Cursor getAllStatuts()
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_STATUTS;
//        Cursor data = db.rawQuery(query,null);
//        return data;
//    }
//    public Cursor getAllReactions()
//    {
//        db = this.getWritableDatabase();
//        String query ="SELECT * FROM "+TABLE_REACTIONS;
//        Cursor data = db.rawQuery(query,null);
//        return data;
//    }
    public  void drop (){
        db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACT_MEDECIN);
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_STATUTS);
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
        db.close();
    }

}
