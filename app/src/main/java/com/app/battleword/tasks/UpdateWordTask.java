package com.app.battleword.tasks;

import android.content.Context;
import android.os.AsyncTask;

public class UpdateWordTask extends AsyncTask<String,Integer,Boolean> {
    private Context context;
    public UpdateWordTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return null;
    }
}
