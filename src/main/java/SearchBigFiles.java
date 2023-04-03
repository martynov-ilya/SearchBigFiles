import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class SearchBigFiles extends RecursiveTask<TreeMap<Long, String>> {

    private final String path;
    private final long bigSizeMeaning;

    public SearchBigFiles(String path, long bigSizeMeaning) {
        this.path = path;
        this.bigSizeMeaning = bigSizeMeaning;
    }

    @Override
    protected TreeMap<Long, String> compute() {

        TreeMap<Long, String> list = new TreeMap<Long, String>();

        List<SearchBigFiles> tasks = new ArrayList<SearchBigFiles>();

        File file = new File(path);

        File content[] = file.listFiles();

        if (content != null) {

            for (int i = 0; i < content.length; i++) {
                if (content[i].isDirectory()) {
                    SearchBigFiles task =  new SearchBigFiles(content[i].getAbsolutePath(), bigSizeMeaning);
                    task.fork();
                    tasks.add(task);
                }

                else {
                    if (checkFileSize(content[i])) {
                        list.put(content[i].length(), content[i].getPath());
                    }
                }
            }
        }

        addResultsFromTasks(list, tasks);
        return list;
    }

    private void addResultsFromTasks(TreeMap<Long, String> list, List<SearchBigFiles> tasks) {

        for (SearchBigFiles item : tasks) {
            list.putAll(item.join());
        }
    }

    private boolean checkFileSize(File file) {

        return file.length() > bigSizeMeaning ? true : false;
    }
}
