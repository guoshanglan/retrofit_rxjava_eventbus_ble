package cc.hisens.hardboiled.patient.db.bean;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


//数据库迁移
public class MyMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        //获取可编辑的图表schema
        RealmSchema schema = realm.getSchema();



        // 从版本1迁移到版本2 --> 添加一个主键id + RealmObject + RealmList
        // model类Person:
        // public Person extends RealmObject {
        //     private String name;
        //     private int age;
        //     @PrimaryKey
        //     private long id;
        //     private Dog favoriteDog;
        //     private RealmList<Dog> dogs;
        //     // getters and setters left out for brevity
        // }
        if (oldVersion == 0) {
            schema.get("Ed")
                    .addField("id", int.class);

            oldVersion++;
        }
    }

}
