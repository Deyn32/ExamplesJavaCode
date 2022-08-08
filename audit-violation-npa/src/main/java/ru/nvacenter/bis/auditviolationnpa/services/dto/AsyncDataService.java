package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.springframework.stereotype.Service;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.*;

import java.util.*;

/**
 * Created by oshesternikova on 14.03.2018.
 * Сервис работы с данными, синхронизирующими многопоточность
 */
@Service
public class AsyncDataService {
    List<AnalyseAsyncData> queue = new ArrayList<>();
    List<WithoutRevAsyncData> wrqueue = new ArrayList<>();
    List<RevLinksAsyncData> rlqueue = new ArrayList<>();

    public AnalyseAsyncData find(){
        if (queue.size() == 0) return null;
        AnalyseAsyncData a = queue.get(queue.size() - 1);
        if (a.isCompleted()) return null;
        return a;
    }

    /**
     * Найти задачу по идентификатору (с удалением, если закончена)
     * @param id
     * @return
     */
    public AnalyseAsyncData find(UUID id){
        AnalyseAsyncData d = queue.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (d != null)
        {
            if (d.isCompleted())
                queue.remove(d);
        }
        return d;
    }
    /***
     * Новый экземпляр
     * @return
     */
    public AnalyseAsyncData create(){
        AnalyseAsyncData d = new AnalyseAsyncData();
        d.setCompleted(false);
        d.setId(UUID.randomUUID());
        d.setStatus("Анализ начался");
        d.setStartTime(new Date());
        queue.add(d);
        return d;
    }

    /**
     * Завершить последнюю задачу в очереди
     * @param arr
     */
    public void finish(List<AnalyseResult> arr){
        AnalyseAsyncData a = queue.get(queue.size() - 1);
        a.setCompleted(true);
        a.setStatus(null);
        a.setData(arr);
        a.setEndTime(new Date());
    }

    /**
     * Завершить последнюю задачу в очереди ошибкой
     * @param ex
     */
    public void finishWithError(Throwable ex){
        AnalyseAsyncData a = queue.get(queue.size() - 1);
        a.setCompleted(true);
        a.setError(ex.getMessage());
        a.setStatus(null);
        a.setData(null);
        a.setEndTime(new Date());
    }

    /**
     * Очистить очередь
     * @return Очищенные данные
     */
    public List<AnalyseAsyncData> clear(){
        List<AnalyseAsyncData> res = new ArrayList<>();
        while (true){
            if (queue.size() == 0) break;
            if (!queue.get(0).isCompleted())
                break;
            res.add(queue.get(0));
            queue.remove(0);
        }
        return res;
    }

    public WithoutRevAsyncData findWR() {
        if (wrqueue.size() == 0) return null;
        WithoutRevAsyncData wr = wrqueue.get(wrqueue.size() - 1);
        if (wr.isCompleted()) return null;
        return wr;
    }

    public WithoutRevAsyncData findWR(UUID id){
        WithoutRevAsyncData wr = wrqueue.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (wr != null)
        {
            if (wr.isCompleted())
                wrqueue.remove(wr);
        }
        return wr;
    }

    public WithoutRevAsyncData createWR(){
        WithoutRevAsyncData wr = new WithoutRevAsyncData();
        wr.setCompleted(false);
        wr.setId(UUID.randomUUID());
        wr.setStatus("Анализ начался");
        wr.setStartTime(new Date());
        wrqueue.add(wr);
        return wr;
    }

    public void finishWR(List<WithoutRevision> arr){
        WithoutRevAsyncData wr = wrqueue.get(wrqueue.size() - 1);
        wr.setCompleted(true);
        wr.setStatus(null);
        wr.setData(arr);
        wr.setEndTime(new Date());
    }

    public void finishWithErrorWR(Throwable ex){
        WithoutRevAsyncData wr = wrqueue.get(wrqueue.size() - 1);
        wr.setCompleted(true);
        wr.setError(ex.getMessage());
        wr.setStatus(null);
        wr.setData(null);
        wr.setEndTime(new Date());
    }



    public RevLinksAsyncData findRL() {
        if(rlqueue.size() == 0) return null;
        RevLinksAsyncData rl = rlqueue.get(rlqueue.size() - 1);
        if(rl.isCompleted()) return null;
        return rl;
    }

    public RevLinksAsyncData findRL(UUID id){
        RevLinksAsyncData rl = rlqueue.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (rl != null)
        {
            if(rl.isCompleted())
                rlqueue.remove(rl);
        }
        return rl;
    }

    public RevLinksAsyncData createRL(){
        RevLinksAsyncData rl = new RevLinksAsyncData();
        rl.setCompleted(false);
        rl.setId(UUID.randomUUID());
        rl.setStatus("Перегруппировка началась. Пожалуйста, дождитесь окончания операции");
        rl.setStartTime(new Date());
        rlqueue.add(rl);
        return rl;
    }

    public void finishRL(List<ViolationRevData> arr){
        RevLinksAsyncData rl = rlqueue.get(rlqueue.size() - 1);
        rl.setCompleted(true);
        rl.setStatus(null);
        rl.setData(arr);
        rl.setEndTime(new Date());
    }

    public void finishWithErrorRL(Throwable ex){
        RevLinksAsyncData rl = rlqueue.get(rlqueue.size() -1);
        rl.setCompleted(true);
        rl.setError(ex.getMessage());
        rl.setStatus(null);
        rl.setData(null);
        rl.setEndTime(new Date());
    }

}
