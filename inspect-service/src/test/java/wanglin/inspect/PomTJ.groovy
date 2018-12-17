package wanglin.inspect

import com.alibaba.fastjson.JSON
import org.junit.Test
import org.springframework.util.StringUtils

class PomTJ {
    @Test
    public void tj(){
        List<File> poms = new ArrayList<>();
        ls_f(new File("D:\\project\\fintech"),poms);
        TreeSet<String> set = new TreeSet<>() ;
        poms.each {f->
            if(f.name.equals("pom.xml")){
                String text = f.text;
                def xml = new XmlSlurper().parseText(text)
                xml.dependencies.dependency.each{d->
                    if(!StringUtils.isEmpty(d.version.text())){
                    set.add(String.format("%s:%s:%s",d.groupId.text(),d.artifactId.text(),d.version.text()))
                    }
                }
            }
        }
        set.sort()
        set.each {j->
            println j;
        }
    }

    void ls_f(File f,List<File> poms) {
        if(!f.isDirectory()){
            poms.add(f)
        }else{
            f.listFiles().each {ff->ls_f(ff,poms)}
        }
    }
    class Dep implements Comparable{
        String group
        String artifactId
        String version

        public Dep(String group,String artifactId,String version){
            this.group= group;
            this.artifactId= artifactId;
            this.version= version;
        }




        @Override
        int compareTo(Object o) {
            return this.group.compareTo(o.group) + this.artifactId.compareTo(o.artifactId)>0?1:0
        }

        @Override
        public String toString() {
            return String.format("%s:%s:%s",group,artifactId,version);
        }
    }
}
