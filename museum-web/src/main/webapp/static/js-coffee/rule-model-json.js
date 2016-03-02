{
    "displayName": "条件对象",
    "description": "作为条件的对象就是我啦",
    "className": "WhenObject",
    "packageInfo": "com.baoyun.pcrf.admin.pma.entitysample",
    "subObjects": [
        {
            "displayName": "条件的名字",
            "description": "一个名字而已",
            "className": "String",
            "packageInfo": "java.lang",
            "subObjects": [],
            "valueType": "STRING",
            "callingExpress": "WhenObject.name"
        },
        {
            "displayName": "条件的年龄",
            "description": "一个年龄而已",
            "className": "String",
            "packageInfo": "java.lang",
            "subObjects": [],
            "valueType": "STRING",
            "callingExpress": "WhenObject.age"
        },
        {
            "displayName": "其他条件的名字",
            "description": "一个其他名字而已",
            "className": "OtherWhenElement",
            "packageInfo": "com.baoyun.pcrf.admin.pma.entitysample",
            "subObjects": [
                {
                    "displayName": "billingday的名字",
                    "description": "一个billingday而已",
                    "className": "String",
                    "packageInfo": "java.lang",
                    "subObjects": [],
                    "valueType": "STRING",
                    "callingExpress": "WhenObject.otherWhenElement.billingDay"
                }
            ],
            "valueType": "OBJECT",
            "callingExpress": "WhenObject.otherWhenElement"
        },
        {
            "displayName": "整型哦",
            "description": "---------",
            "className": "Integer",
            "packageInfo": "java.lang",
            "subObjects": [],
            "valueType": "INTEGER",
            "callingExpress": "WhenObject.i"
        },
        {
            "displayName": "长整型哦",
            "description": "++++++",
            "className": "Long",
            "packageInfo": "java.lang",
            "subObjects": [],
            "valueType": "LONG",
            "callingExpress": "WhenObject.j"
        }
    ],
    "valueType": "OBJECT",
    "callingExpress": "WhenObject"
}