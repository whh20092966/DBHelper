package net.qiushao.lib;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import net.qiushao.lib.dbhelper.DBFactory;
import net.qiushao.lib.dbhelper.DBHelper;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = "dbhelper";

    public ApplicationTest() {
        super(Application.class);
    }

    public void testInsert() {
        Log.i(TAG, "test insert start");
        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        String id = "1";
        String name = "shaoqiu";
        int age = 26;
        boolean marry = false;
        float height = 165.0f;
        double weight = 53.25;
        db.insert(new Person(id, name, age, marry, height, weight));
        Collection<Object> pesons = db.query(null, null);
        for(Object object : pesons) {
            Person person = (Person) object;
            assertEquals(person.id, id);
            assertEquals(person.name, name);
            assertEquals(person.age, age);
            assertEquals(person.marry, marry);
            assertEquals(person.height, height);
            assertEquals(person.weight, weight);
        }

        Log.i(TAG, "test insert pass");
    }

    public void testInsertAll() {
        Log.i(TAG, "test insertAll start");
        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        Collection<Object> persons = new LinkedList<>();
        int count = 1000;
        for(int i = 0; i < count; i++) {
            Person person = new Person("id" + i, "name" + i, i, false, 168.0f, 54.00);
            persons.add(person);
        }

        long start = System.currentTimeMillis();
        db.insertAll(persons);
        long end = System.currentTimeMillis();
        Log.d(TAG, "insert " + count + " object span time : "  + (end - start));

        persons = db.query(null, null);
        assertEquals(persons.size(), count);

        for(Object object : persons) {
            Person person = (Person) object;
            assertEquals(person.id, "id" + person.age);
            assertEquals(person.name, "name" + person.age);
            assertEquals(person.marry, false);
            assertEquals(person.height, 168.0f);
            assertEquals(person.weight, 54.00);
        }

        Log.i(TAG, "test insertAll pass");
    }

    public void testInsertOrIgnore() {
        Log.i(TAG, "test insertOrIgnore start");
        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        String id = "1";
        String name = "shaoqiu";
        int age = 26;
        boolean marry = false;
        float height = 165.0f;
        double weight = 53.25;
        db.insert(new Person(id, name, age, marry, height, weight));

        marry = true;
        age = 27;
        weight = 56.00;
        db.insertOrIgnore(new Person(id, name, age, marry, height, weight));

        Collection<Object> persons = db.query(null, null);
        assertEquals(persons.size(), 1);

        for(Object object : persons) {
            Person person = (Person) object;
            assertEquals(person.marry, false);
            assertEquals(person.age, 26);
            assertEquals(person.weight, 53.25);
        }
        Log.i(TAG, "test insertOrIgnore pass");
    }

    public void testInsertOrReplace() {
        Log.i(TAG, "test insertOrReplace start");

        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        String id = "1";
        String name = "shaoqiu";
        int age = 26;
        boolean marry = false;
        float height = 165.0f;
        double weight = 53.25;
        db.insert(new Person(id, name, age, marry, height, weight));

        marry = true;
        age = 27;
        weight = 56.00;
        db.insertOrReplace(new Person(id, name, age, marry, height, weight));

        Collection<Object> persons = db.query(null, null);
        assertEquals(persons.size(), 1);

        for(Object object : persons) {
            Person person = (Person) object;
            assertEquals(person.marry, marry);
            assertEquals(person.age, age);
            assertEquals(person.weight, weight);
        }

        Log.i(TAG, "test insertOrReplace pass");
    }


    public void testDelete() {
        Log.i(TAG, "test delete start");

        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        db.insert(new Person("1", "shaoqiu", 26, false, 165.0f, 53.00));
        db.insert(new Person("2", "qiushao", 26, false, 165.0f, 53.00));
        db.insert(new Person("3", "junday", 23, false, 165.0f, 52.00));

        db.delete("id=?", new String[]{"2"});
        db.delete("name=? and age=?", new String[]{"junday", String.valueOf(23)});

        Collection<Object> objects = db.query(null, null);
        assertEquals(objects.size(), 1);

        Log.i(TAG, "test delete pass");
    }

    public void testClean() {
        Log.i(TAG, "test clean start");

        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        db.insert(new Person("1", "shaoqiu", 26, false, 165.0f, 53.00));
        db.insert(new Person("2", "qiushao", 26, false, 165.0f, 53.00));
        db.insert(new Person("3", "junday", 23, false, 165.0f, 52.00));

        db.clean();

        Collection<Object> objects = db.query(null, null);
        assertEquals(objects.size(), 0);

        Log.i(TAG, "test clean pass");
    }

    public void testQuery() {
        Log.i(TAG, "test query start");

        DBHelper db = DBFactory.getInstance(getContext()).getDBHelper(Person.class);
        db.clean();

        db.insert(new Person("1", "shaoqiu", 26, false, 165.0f, 53.00));
        db.insert(new Person("2", "qiushao", 26, false, 165.0f, 53.00));
        db.insert(new Person("3", "junday", 23, false, 165.0f, 52.00));
        db.insert(new Person("4", "qiushao", 27, false, 165.0f, 53.00));
        db.insert(new Person("5", "qiushao", 28, false, 165.0f, 53.00));

        Collection<Object> objects = db.query("name=?", new String[] {"qiushao"});
        assertEquals(objects.size(), 3);

        objects = db.query("age>?", new String[] {String.valueOf(23)});
        assertEquals(objects.size(), 4);

        Log.i(TAG, "test query end");
    }

}