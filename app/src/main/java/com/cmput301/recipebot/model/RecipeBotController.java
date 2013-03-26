/*
 * Copyright 2013 Adam Saturna
 *  Copyright 2013 Brian Trinh
 *  Copyright 2013 Ethan Mykytiuk
 *  Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cmput301.recipebot.model;

import android.content.Context;
import com.cmput301.recipebot.client.ESClient;

import java.util.List;

/**
 * A controller class that makes it transparent when switching betwen network and local calls.
 */
public class RecipeBotController {

    DatabaseHelper dbHelper;
    ESClient esClient;

    public RecipeBotController(Context context) {
        dbHelper = new DatabaseHelper(context);
        esClient = new ESClient();
    }

    public List<Ingredient> loadPantry() {
        return dbHelper.getAllPantryItems();
    }

    public void insertPantryItem(Ingredient ingredient) {
        dbHelper.insertPantryItem(ingredient);
    }

    public void deletePantryItem(String name) {
        dbHelper.deletePantryItem(name);
    }

}
