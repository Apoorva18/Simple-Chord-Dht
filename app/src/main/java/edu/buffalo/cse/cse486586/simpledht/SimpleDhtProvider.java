package edu.buffalo.cse.cse486586.simpledht;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class SimpleDhtProvider extends ContentProvider {
    static final String TAG =  SimpleDhtActivity.class.getSimpleName();
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
  //public static int noofneeded ;
   public static ArrayList<String > queryans =  new ArrayList<String>();
     final int SERVER_PORT = 10000;
    String portStr;
    String myPort;
    String pre ;
    String suc ;
     int count =0;
     //https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=2ahUKEwi2qNW3msrhAhVCmVkKHYWgDqIQFjAAegQIARAB&url=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-util-concurrent%2Fblockingqueue.html&usg=AOvVaw3DnsnEmUB4_XHVX1ZEapUM
     BlockingQueue<String> b =new ArrayBlockingQueue<String>(1);
    BlockingQueue<String> w =new ArrayBlockingQueue<String>(1);


    ArrayList<Node> nodes =new ArrayList();
    //static ArrayList<> rows=
    ArrayList<String> Keystoshow = new ArrayList<String>();
    int what = 0;
    Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        File dir = getContext().getFilesDir();
       //  https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=11&cad=rja&uact=8&ved=2ahUKEwiwk9Cg88nhAhUwn-AKHU4HDyYQFjAKegQIAhAB&url=https%3A%2F%2Fstackoverflow.com%2Fquestions%2F3554722%2Fhow-to-delete-internal-storage-file-in-android&usg=AOvVaw3MALgBj65Q14XwruSdlQqJ
        if(Keystoshow.contains(selection)){
            //File dir = getContext().getFilesDir().
            File file = new File(dir, selection);
            boolean deleted = file.delete();
        }
        else if (selection.contains("@")) {
            int i=0;
            while(Keystoshow.size()>0){
                File file = new File(dir, Keystoshow.get(i));
                boolean deleted = file.delete();
                Keystoshow.remove(i);
                i++;
            }

        }


        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        Log.e(  TAG,""+myPort);

        ContentValues newvalue = new ContentValues();
        String keyValue = values.getAsString("key");
        String FileName = keyValue;
      //  Log.e("key",keyValue);

        Log.e(TAG, "HERE2");


        String msg = values.getAsString("value");
        //Log.e("val",msg);
        String total = keyValue + " " + msg;


        try {
            String hash = genHash(keyValue);

            if (pre == null && suc == null) {
                FileOutputStream outputStream;
                outputStream = null;
                Keystoshow.add(FileName);
                Log.e(TAG, "HERE3");
                try {
                    outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                    outputStream.write(msg.getBytes());
                    Log.e(TAG, "HERE4");
                    outputStream.close();
                    Log.e(TAG,"here12");

                } catch (FileNotFoundException e) {
                    Log.d("TAG", "FIle not found");
                } catch (IOException e) {
                    Log.d("Tag", "IOexception");
                }
            }
           /* else if ((suc.equals( pre)) && (!suc.equals(null))) {
                if ((hash.compareTo(genHash(portStr)) > 0) && (hash.compareTo(genHash(suc)) < 0)) {
                    what = 2;
                    Log.e(TAG,"WRONG1");
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);
                }
                else if ((hash.compareTo(genHash(portStr)) > 0) && (hash.compareTo(genHash(suc)) > 0 && (genHash(portStr).compareTo(genHash(pre))<0))) {
                    FileOutputStream outputStream;
                    outputStream = null;
                    Log.e("addedhere","added");
                    try {
                        Keystoshow.add(FileName);
                        outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                        outputStream.write(msg.getBytes());
                        outputStream.close();

                    } catch (FileNotFoundException e) {
                        Log.d("TAG", "FIle not found");
                    } catch (IOException e) {
                        Log.d("Tag", "IOexception");
                    }
                }
                else if ((hash.compareTo(genHash(portStr)) < 0) && (hash.compareTo(genHash(suc)) < 0 && (genHash(portStr).compareTo(genHash(suc))<0))){
                    FileOutputStream outputStream;
                    outputStream = null;
                    Log.e("addedhere","added");
                    try {
                        Keystoshow.add(FileName);
                        outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                        outputStream.write(msg.getBytes());
                        outputStream.close();

                    } catch (FileNotFoundException e) {
                        Log.d("TAG", "FIle not found");
                    } catch (IOException e) {
                        Log.d("Tag", "IOexception");
                    }

                }

            }/* else if ((hash.compareTo(genHash(portStr)) < 0)&&(hash.compareTo(genHash(pre)) < 0) && (pre.compareTo(genHash(portStr))>0)) {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);


            }*/

         else if ((hash.compareTo(genHash(portStr)) <=0)&&(hash.compareTo(genHash(pre)) <=0)&& (genHash(pre).compareTo(genHash(portStr))>0) ) {
                FileOutputStream outputStream;
                outputStream = null;
                try {
                    Keystoshow.add(FileName);
                    outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                    outputStream.write(msg.getBytes());
                    outputStream.close();

                } catch (FileNotFoundException e) {
                    Log.d("TAG", "FIle not found");
                } catch (IOException e) {
                    Log.d("Tag", "IOexception");
                }

            }
            /*
            else if ((hash.compareTo(genHash(portStr)) < 0)&&(hash.compareTo(genHash(suc)) > 0)&&(suc.compareTo(genHash(portStr))<0) ) {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);

            }
*/

            else if ((hash.compareTo(genHash(portStr)) > 0)&&(hash.compareTo(genHash(pre)) > 0) && (genHash(pre).compareTo(genHash(portStr))>0)) {
                FileOutputStream outputStream;
                Log.e("tag","I dont know");
                outputStream = null;
                try {
                    Keystoshow.add(FileName);
                    outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                    outputStream.write(msg.getBytes());
                    outputStream.close();

                } catch (FileNotFoundException e) {
                    Log.d("TAG", "FIle not found");
                } catch (IOException e) {
                    Log.d("Tag", "IOexception");
                }

            }
            else if ((hash.compareTo(genHash(pre)) > 0) && (hash.compareTo(genHash(portStr)) <=0)) {
                FileOutputStream outputStream;
                Log.e("tag","wrong2");
                outputStream = null;
                try {
                    Keystoshow.add(FileName);
                    outputStream = getContext().openFileOutput(FileName, MODE_PRIVATE);
                    outputStream.write(msg.getBytes());
                    outputStream.close();

                } catch (FileNotFoundException e) {
                    Log.d("TAG", "FIle not found");
                } catch (IOException e) {
                    Log.d("Tag", "IOexception");
                }
            }

         /*   else if ((hash.compareTo(genHash(portStr)) > 0)&&(hash.compareTo(genHash(suc)) < 0) ) {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);

            }else if ((hash.compareTo(genHash(portStr)) > 0)&&(hash.compareTo(genHash(suc)) > 0) && (portStr.compareTo(genHash(suc))>0)) {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);

            }else if ((hash.compareTo(genHash(portStr)) > 0)&&(hash.compareTo(genHash(suc)) > 0) ) {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);

            }
*/
         else
            {
                what = 2;//passing to successor
                Log.e("tag","wrong2kkk");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, total, myPort);

            }







        Log.e(TAG,"here5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
        }
    return null;

    }


    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
       // super.onCreate(savedInstanceState);

    TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
    final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
    System.out.println("PORT!!!!!!::" + portStr);
    //if (portStr == "5554") {
      //  Node n = new Node("5554", genHash("5554"), null, null);
        //Log.e(TAG, "Adding firstnode");
        //nodes.add(n);
    //}

    try {

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
    } catch (IOException e) {

        Log.e(TAG, "Can't create a ServerSocket");
    }
    Log.e(TAG, portStr);
    if (!portStr.equals("5554")) {
        Log.e(TAG, "sending join request");
        String msg = "ADDME" + " " + portStr;
        Log.e("msg", msg);
        what = 3;//for requesting 5554 to add
        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

    }
    // if(portStr.equals("5554")){
    //   what =32;
    // new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "/", myPort);
    //}
    //  new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

      return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        FileInputStream newFile = null;
        MatrixCursor matrixCursor =  new MatrixCursor(new String[]{"key", "value"});
       // Log.e("projection", projection[0]);
        //Log.e("query print",uri.toString());
        //for(int i=0;i<projection.length;i++){
     //      Log.e("query print",projection[i]);}
    //    Log.e("query print",selection);
      //  for(int i=0;i<selectionArgs.length;i++){
        //    Log.e("query print",selectionArgs[i]);}
        //Log.e("query print",sortOrder);

        // TODO Auto-generated method stub
        if(!(selection.contains("*")||selection.contains("@")))
        {        //noofneeded = noofneeded +1;

            Log.e("here","indide selection");
            //FileInputStream newFile = null;
            MatrixCursor matrixCursor1 = null ;

                Log.e(TAG,newFile+"outside");
                if(selection.contains("qresult")){
                    matrixCursor1 = new MatrixCursor(new String[]{"key", "value"});
                    String[] s =selection.split(" ");
                    Log.e(s[2],s[3]);

                    matrixCursor1.addRow(new String[]{s[2],s[3]});
                    Log.e("PRinting","got  rquery");

                    //inputStreamReader.close();
                    try {
                        b.put(s[2]+":"+s[3]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    matrixCursor =matrixCursor1;
                }
               else if(Keystoshow.contains(selection)  ) {
                    try {
                        newFile = getContext().openFileInput(selection);
                        Log.e(TAG, newFile + "count value");
                        matrixCursor1 = new MatrixCursor(new String[]{"key", "value"});
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        if(sortOrder ==null) {
                            //matrixCursor = new MatrixCursor(new String[]{"key", "value"});

                            Log.e(TAG,"SORTORDER NULL");
                            matrixCursor1.addRow(new String[]{selection, bufferedReader.readLine()});


                            inputStreamReader.close();

                            matrixCursor = matrixCursor1;
                        }
                        else{

                            what =100;
                            Log.e(TAG,"sending final");
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,  "qresult" +" "+ sortOrder+ " " +selection+" "+ bufferedReader.readLine(), myPort);

                        }






                    } catch (FileNotFoundException e) {
                        Log.d("Tag", "File notfound");

                    } catch (IOException e) {
                        Log.d("Tag", "IO exception");
                    }
                    Log.v("query", selection);
                }

                   /* else  if(noofneeded==nodes.size()){
                    matrixCursor = new MatrixCursor(new String[]{"key", "value"});
                    for(int i =0;i<queryans.size();i++){
                       String[] q =  queryans.get(i).split(" ");
                       Log.e("ans","at 1st node again");

                        matrixCursor.addRow(new String[]{q[0],q[1]});
                    }
                   // inputStreamReader.close();
                    noofneeded = 0;
                    queryans.clear();
                    return matrixCursor;
                }
*/
            else{
                if(sortOrder==null){


                String passquery= "query="+null+","+null+","+selection+","+null+","+portStr;
                what = 5;//for query passing
                Log.e(TAG,"PASSING QUERY");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);
                    matrixCursor = new MatrixCursor(new String[]{"key", "value"});
                    String s= "";
                    try {
                        s = b.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String[] k = s.split(":");
                    matrixCursor.addRow(new String[]{k[0],k[1]});
                   return matrixCursor;
                }
                else{
                    String passquery= "query="+null+","+null+","+selection+","+null+","+sortOrder;
                    what = 5;//for query passing
                    Log.e(TAG,"PASSING QUERY");
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);
                }

        }


        }
        if(selection.contains("*")) {
           // Log.e(TAG,"IN QUERY *");
            //FileInputStream newFile = null;
           // MatrixCursor matrixCursor = null;
           // matrixCursor = new MatrixCursor(new String[]{"key", "value"});
            Log.d(TAG,"AllKeyValue");
            String passquery = "";
            try {
                if(suc==null) {
                    for (int i = 0; i < Keystoshow.size(); i++) {
                        newFile = getContext().openFileInput(Keystoshow.get(i));
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                        matrixCursor.addRow(new String[]{Keystoshow.get(i), bufferedReader.readLine()});

                        inputStreamReader.close();


                    }
                    return matrixCursor;
                }
                    else if(sortOrder == null && suc!=null) {
                       // String s="";
                    String msg1="";
                    for (int i = 0; i < Keystoshow.size(); i++) {
                            newFile = getContext().openFileInput(Keystoshow.get(i));
                            InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                            msg1+= Keystoshow.get(i)+ " "+ bufferedReader.readLine()+"|";
                            Log.d(TAG, "FileContent:"+msg1);
                            //matrixCursor.addRow(new String[]{Keystoshow.get(i), bufferedReader.readLine()});




                        }
                        passquery =   msg1;

                        what = 12;//for query passing
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);
                        Log.d(TAG, " Called for what=12");
                        String s= "";
                        try {
                            s = w.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String[] k = s.split("\\|");
                        for(int i =0;i<k.length;i++){
                           String g[]=  k[i].split(" ");
                        matrixCursor.addRow(new String[]{g[0],g[1]});
                        }
                        w.clear();
                        return matrixCursor;



                }
              /*  else if(sortOrder.contains("#")&&(sortOrder.split("#")[0]!=portStr)){
                    //String[] m = sortOrder.split(":");

                    String msg = " ";
                    for (int i = 0; i < Keystoshow.size(); i++) {
                        newFile = getContext().openFileInput(Keystoshow.get(i));
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        msg += Keystoshow.get(i) + " " + bufferedReader.readLine() + "|";

                    }
                    String k = sortOrder+msg;
                    passquery = selection +"," + k;
                    //msg = m[1]+msg;

                       //for query passing
                    if(sortOrder.split("#")[0].equals(suc))
                    {
                        what =101;

                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);

                    }
                    else {
                        what = 5;
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);
                    }
                }
               /* else if(sortOrder.contains("#")&&(sortOrder.split("#")[0].equals(portStr))){
                      Log.e("####################################","####################");
                    String[] m = sortOrder.split("#");
                    w.put(m[1]);
                    matrixCursor = new MatrixCursor(new String[]{"key", "value"});
                    String[] k = m[1].split("\\|");
                    for(int i =0;i<k.length;i++){
                        String g[]=  k[i].split(" ");
                        matrixCursor.addRow(new String[]{g[0],g[1]});
                    }
                    return matrixCursor;



                }
               /* else if(sortOrder != portStr && sortOrder!=null) {
                   String msg = " ";
                    for (int i = 0; i < Keystoshow.size(); i++) {
                        newFile = getContext().openFileInput(Keystoshow.get(i));
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        msg += Keystoshow.get(i) + "#" + bufferedReader.readLine() + "|";

                    }
                    passquery = ""+null + "," + projection + "," + selection + "," + selectionArgs + "," + sortOrder;
                    if (suc != null) {
                        what = 10;//for query passing
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery+" "+"contains"+" "+ msg,myPort);
                    }
                }*/
               /*else if(sortOrder == null && suc!=null) {
                    String s="";
                    for (int i = 0; i < Keystoshow.size(); i++) {
                        newFile = getContext().openFileInput(Keystoshow.get(i));
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                        matrixCursor.addRow(new String[]{Keystoshow.get(i), bufferedReader.readLine()});

                        inputStreamReader.close();


                    }
                    passquery = "query=" + null + "," + null + "," + selection + "," + null + "," + portStr;
                    if (suc != null) {
                        what = 5;//for query passing
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, myPort);

                    }
                    try {
                        s = w.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String[] e = s.split(",");
                    for(int i=0;i<e.length;i++){
                        String [] y = e[i].split(" ");
                        matrixCursor.addRow(new String[]{y[0], y[1]});
                    }
                    return matrixCursor;
                }
                else if(sortOrder != portStr && sortOrder!=null){
                    if(selectionArgs ==null){
                        selectionArgs = new String [1];
                    }
                    for (int i = 0; i < Keystoshow.size(); i++) {
                        newFile = getContext().openFileInput(Keystoshow.get(i));
                        InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        selectionArgs[0] += Keystoshow.get(i)+ " "+ bufferedReader.readLine()+",";

                    }
                    passquery ="query="+null+","+projection+","+selection+","+selectionArgs+","+sortOrder;
                    if(suc!=null  ) {
                        what = 5;//for query passing
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, passquery, sortOrder);
                    }

                    }
                    else if(sortOrder.equals( portStr)){
                        //String[] k = projection[1].split(",");

                            try {
                                w.put(selectionArgs[0]);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


*/

            } catch (FileNotFoundException e) {
                Log.d("Tag", "File notfound");

            } catch (IOException e) {
                Log.d("Tag", "IO exception");
            }
            Log.v("query", selection);
            return matrixCursor;
        }
        if(selection.contains("@")) {

            //FileInputStream newFile = null;
            //MatrixCursor matrixCursor = null;
            //matrixCursor = new MatrixCursor(new String[]{"key", "value"});


            try {
                for(int i=0;i<Keystoshow.size();i++){
                    newFile = getContext().openFileInput(Keystoshow.get(i));
                    InputStreamReader inputStreamReader = new InputStreamReader(newFile);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    Log.e("showing of 5554","5554");
                    matrixCursor.addRow(new String[]{Keystoshow.get(i), bufferedReader.readLine()});

                    inputStreamReader.close();
                }

            } catch (FileNotFoundException e) {
                Log.d("Tag", "File notfound");

            } catch (IOException e) {
                Log.d("Tag", "IO exception");
            }
            Log.v("query", selection);
            return matrixCursor;
        }
    Log.e("returning","null");

   return matrixCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            final ServerSocket serverSocket = sockets[0];
            TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
            final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));

            try {
                while(true){
                    Socket sc = serverSocket.accept();

                    InputStream inl = sc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inl);
                    BufferedReader br = new BufferedReader(isr);
                    String msg = br.readLine();

                    if (msg.contains("finished")) {
                        FileInputStream newFile = null;
                        InputStreamReader inputStreamReader = null;
                        String res = "";
                        try {
                            for(int i= 0;i<Keystoshow.size();i++)
                        {


                                newFile = getContext().openFileInput(Keystoshow.get(i));

                                 inputStreamReader = new InputStreamReader(newFile);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                res +=Keystoshow.get(i) + " " + bufferedReader.readLine() + "|";


                            }
                                //matrixCursor.addRow(new String[]{Keystoshow.get(i), bufferedReader.readLine()});

                            ///inputStreamReader.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        //byte[]  b;
                      String  b = res+"!"+suc;
                        Log.e(TAG,"RES!!!!!!!!!!!!"+res);
                        //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(sc.getOutputStream());
                        //outputStreamWriter.write(b);
                        //outputStreamWriter.flush();
                        DataOutputStream d= new DataOutputStream(sc.getOutputStream());
                        d.writeBytes(b +"\n");
                        d.flush();


                    }

                   else if(msg.contains("#")){
                        //String[] q = msg.split("=");
                        //Log.e(TAG,msg);
                        final ContentResolver mContentResolver = getContext().getContentResolver();
                        final ContentValues mContentValues= new ContentValues();
                        //Log.e(TAG,q[1]);
                        String k[]=msg.split(",");
                        Log.e("inside #",k[0]);
                        Log.e("outiside #",k[1]);
                        //String h[] = k[1].split("#");// 5554 contains  msgs
                       // Log.e("h2::::::;",h[2]);  //message
                        //String m = h[2];
                       // String [ ] a = new String[1];
                        //a[0] = k[1];
                        Uri tempuri = mUri;
                        String[] projection =null;
                        String selection = k[0];
                        String [] selectargs =   null;
                       // String sortorder= h[0]+":"+ m;


                        mContentResolver.query(tempuri,projection,selection,selectargs,k[1]);}


                    else if(msg.contains("query"))
                    {
                        String[] q = msg.split("=");
                        Log.e(TAG,msg);
                        final ContentResolver mContentResolver = getContext().getContentResolver();
                        final ContentValues mContentValues= new ContentValues();
                        Log.e(TAG,q[1]);
                        String k[]=q[1].split(",");
                        for(int i =0;i<k.length;i++){

                            Log.e(TAG,k[i]);
                            Log.e(TAG,i+"");
                        }
                        String [ ] a = new String[1];
                        a[0] = k[1];
                        Uri tempuri = mUri;
                        String[] projection =null;
                        String selection = k[2];
                        String [] selectargs =   null;
                        String sortorder= k[4];



                       mContentResolver.query(tempuri,projection,selection,selectargs,sortorder);


                    }
                   else if(msg.contains(":")){
                        String msged[]= msg.split(":");
                        suc = msged[1];
                        pre = msged[2];
                        Log.e("setting suc and pre",suc);
                        Log.e("setting suc and pre",pre);

                    }

                   else if(portStr.equals("5554")&& msg.contains("ADDME")){
                        //String msg =  br .readLine();
                        what =-1;//forjoing
                        String key = msg.split(" ")[0];
                        String val = msg.split(" ")[1];
                        String hash = genHash(val);
                        Log.e(TAG,"JOINING");

                        if(key.equals("ADDME"))
                        { if(count ==0){
                            Node n = new Node("5554", genHash("5554"), null, null);
                            Log.e(TAG, "Adding firstnode");
                            nodes.add(n);
                        }
                            count ++;
                            Node node = new Node(val,hash,null,null);
                            Log.e(TAG,"INSIDE"+count);
                            nodes.add(node);
                            Collections.sort( nodes);
                            if(count==1){
                                node.successor = "5554";
                                node.predecessor ="5554";
                                for(int i= 0;i<nodes.size();i++){
                                   if( nodes.get(i).portNo.equals("5554")){
                                       Node z = nodes.get(i);
                                       z.successor = val;
                                       z.predecessor =val;
                                       Log.e("Inside 1",val);
                                    }

                                }

                            }
                            else{
                                for(int i=0;i<nodes.size();i++){
                                    Node n = nodes.get(i);
                                    if(i==0){
                                        n.predecessor = nodes.get(nodes.size()-1).portNo;
                                        n.successor= nodes.get(1).portNo;

                                    }
                                    else if( i ==(nodes.size()-1)){
                                        n.predecessor = nodes.get(i-1).portNo;
                                        n.successor = nodes.get(0).portNo;
                                    }
                                    else {
                                        n.predecessor = nodes.get(i - 1).portNo;
                                        n.successor = nodes.get(i + 1).portNo;

                                    }
                                }

                            }
                            for(int i=0;i<nodes.size();i++){
                                Log.e("just",nodes.get(i).portNo);
                            }
                            if(portStr.equals("5554")&& what == -1) {
                                Log.e(TAG, "Broadcasting");

                                BroadcastSP(nodes);
                            }
                        }


                     /*   else{
                            if(count == 0){
                                publishProgress(msg);
                            }
                            else{
                                if(count ==1){
                                    if((hash.compareTo(nodes.get(0).geth())>0) &&( hash.compareTo(nodes.get(1).geth())<0))
                                    {

                                    }
                                    else{
                                        publishProgress(msg);
                                    }
                                }
                                else{
                                    if((hash.compareTo(nodes.get(0).geth())>0) &&( hash.compareTo(nodes.get(1).geth())<0)){

                                    }
                                    else if((hash.compareTo(nodes.get(0).geth())<0) &&( hash.compareTo(nodes.get(1).geth())>0))
                                    { publishProgress(msg);
                                    }
                                }
                            }

                        }*/
                    }
                    else if( msg.contains("-")){
                        String[] msg2 = msg.split("-");
                        String[] msg1 = msg2[1].split(" ");
                        Log.e(TAG,"GOT MSG" );
                        Log.e(TAG,msg1[0]);
                        Log.e(TAG,msg1[1]);
                        final ContentResolver mContentResolver = getContext().getContentResolver();
                        final ContentValues mContentValues= new ContentValues();
                        // Keystoshow.add(key);
                        mContentValues.put("key",msg1[0]);
                        mContentValues.put("value",msg1[1]);
                        mContentResolver.insert(mUri,mContentValues);

                    }
                    else if(msg.contains("qresult")){
                        //String[] q = msg.split(" ");
                        Log.e(TAG,msg);
                        final ContentResolver mContentResolver = getContext().getContentResolver();
                        final ContentValues mContentValues= new ContentValues();
                        //Log.e(TAG,q[1]);
                        //String k[]=q[1].split(",");
                        //for(int i =0;i<k.length;i++){
                           // Log.e(TAG,k[i]);
                           // Log.e(TAG,i+"");
                        //}
                       String[] p =  msg.split(" ");
                        Uri tempuri = mUri;
                        String[] projection =null;
                        String selection = msg;
                        String [] selectargs =   null;
                        String sortorder= p[1];
                        Log.e("result",sortorder);



                        mContentResolver.query(tempuri,projection,selection,selectargs,sortorder);
                    }


                  //  publishProgress(br.readLine());
                }

            } catch (IOException e) {
                Log.e(TAG, "IDK");
            }catch(NoSuchAlgorithmException e){
                Log.e(TAG,e.getMessage());
            }




            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */


            return null;
        }

        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            //TextView remoteTextView = (TextView) findViewById(R.id.remote_text_display);
            //remoteTextView.append(strReceived + "\t\n");
            //TextView localTextView = (TextView) findViewById(R.id.local_text_display);
            //localTextView.append("\n");

            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             *
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */

            /**
             * buildUri() demonstrates how to build a URI for a ContentProvider.
             *
             * @param scheme
             * @param authority
             * @return the URI
             */
            String key = strReceived.split(" ")[0];
            String value  = strReceived.split(" ")[1];

           final ContentResolver mContentResolver = getContext().getContentResolver();

            final Uri mUri;
            final ContentValues mContentValues= new ContentValues();

            mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");

            //String filename = "SimpleMessengerOutput";
            Keystoshow.add(key);
            mContentValues.put("key",key);
            mContentValues.put("value",value);
            mContentResolver.insert(mUri,mContentValues);
           // k = k + 1;

            //String  = strReceived + "\n";

            //FileOutputStream outputStream;

            //try {
            //  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            //outputStream.write(string.getBytes());
            //outputStream.close();
            //} catch (Exception e) {
            //   Log.e(TAG, "File write failed");
            //}


        }
    }
     public void BroadcastSP(ArrayList<Node> nodes){
        for(int i =0;i<nodes.size();i++){
            String port = nodes.get(i).portNo;
            String success =nodes.get(i).successor;
            String pre = nodes.get(i).predecessor;
            String msg = port +":" +success +":"+pre;
            what =-1;
            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);
        }
     }

    private class ClientTask extends AsyncTask<String, Void,Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            try {

                TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
                final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
                String[] remotePort = new String[5];
                remotePort[0] = REMOTE_PORT0;
                remotePort[1] = REMOTE_PORT1;
                remotePort[2] = REMOTE_PORT2;
                remotePort[3] = REMOTE_PORT3;
                remotePort[4] = REMOTE_PORT4;
                String msgToSend = msgs[0];
                if(what ==5){






                    Log.e("here","what= 5");
                   // String msg []= msgToSend.split(" ");
                    String Porttosend = String.valueOf((Integer.parseInt(suc) * 2));

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));
                    byte[]  b;
                    b = msgToSend.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();

                    socket.close();
                }
                if (what ==3){//for sending add me
                    String msg []= msgToSend.split(" ");
                    String Porttosend = String.valueOf((Integer.parseInt("5554") * 2));

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));
                    byte[]  b;
                    b = msgToSend.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();

                    socket.close();
                }

                if(what == -1){
                String msg []= msgToSend.split(":");
                String Porttosend = String.valueOf((Integer.parseInt(msg[0]) * 2));
                    Log.e("in client task","sending");
                Log.e("TAG",Porttosend);
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));

                    Log.e("in client task","sending");

                    /*System.out.println(b);*/
                    /*
                     * TODO: Fill in your client code that sends out a message.
                     */
                    byte[]  b;
                    b = msgToSend.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();

                    socket.close();
                }
                    if(what ==2){
                        String Porttosend = String.valueOf((Integer.parseInt(suc)*2));

                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                Integer.parseInt(Porttosend));


                        Log.e(TAG,"WHAT=2");
                        /*System.out.println(b);*/
                        /*
                         * TODO: Fill in your client code that sends out a message.
                         */
                        String m = "msg-"+msgToSend;
                        byte[]  b;
                        b = m.getBytes();
                        OutputStream outs = socket.getOutputStream();
                        outs.write(b);
                        outs.close();

                        socket.close();
                    }
                if(what ==100){
                    String[] send = msgToSend.split(" ");
                    String Porttosend = String.valueOf((Integer.parseInt(send[1])*2));

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));


                    Log.e(TAG,"WHAT=2");
                    /*System.out.println(b);*/
                    /*
                     * TODO: Fill in your client code that sends out a message.
                     */
                    String m = msgToSend;
                    byte[]  b;
                    b = m.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();
                    socket.close();

                }
                if(what ==10){
                    String[] send = msgToSend.split(" ");
                    String Porttosend = String.valueOf((Integer.parseInt(suc)*2));

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));


                    Log.e(TAG,"WHAT=2");
                    /*System.out.println(b);*/
                    /*
                     * TODO: Fill in your client code that sends out a message.
                     */
                    String m = msgToSend;
                    byte[]  b;
                    b = m.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();
                    socket.close();
                }
                if(what==101){
                    String[] send = msgToSend.split(" ");
                    String Porttosend = String.valueOf((Integer.parseInt(suc)*2));

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(Porttosend));

                   String p=  "finished"+ ":"+ msgToSend.split("#")[1];
                    Log.e(TAG,"WHAT=2");
                    /*System.out.println(b);*/
                    /*
                     * TODO: Fill in your client code that sends out a message.
                     */
                    String m = msgToSend;
                    byte[]  b;
                    b = m.getBytes();
                    OutputStream outs = socket.getOutputStream();
                    outs.write(b);
                    outs.close();

                    socket.close();

                }
                if(what==12){

                    String qport = portStr;
                    Log.e(TAG,"qport:::::::"+qport);
                    String result="";
                    String qwerty =suc;

                    while(!qwerty.equals(qport)){
                       String Porttosend = String.valueOf((Integer.parseInt(qwerty)*2));

                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                Integer.parseInt(Porttosend));
                        String m = "finished"+ ":" +msgToSend;
                        //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                        //outputStreamWriter.write(m);
                        //outputStreamWriter.flush();

                        DataOutputStream d= new DataOutputStream(socket.getOutputStream());
                        d.writeBytes(m +"\n");
                        d.flush();
                        /*byte[]  b;
                        b = m.getBytes();
                        OutputStream outs = socket.getOutputStream();
                        outs.write(b);
                        outs.flush();
                        outs.close();*/
                        Log.e(TAG,"Sent to Server of Successor");

                        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                        BufferedReader br =new BufferedReader(isr);
                        String msg = br.readLine();
                        String h[ ]=msg.split("!");
                        Log.e(TAG,"QWERTY:"+h[1]);
                        Log.e(TAG,"RESULT"+h[0]);
                        qwerty =h[1];
                        result +=h[0];

                    }
                    Log.e("result!!!!!!!!",result);
                    w.put(msgToSend+result);

                }



        } catch (UnknownHostException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }
}
