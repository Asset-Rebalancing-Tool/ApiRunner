package ARApi.Scaffold.Database.Entities;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface RepoInsertOnDuplicateReturn<T> {

    T GetDuplicate(T failedInsert);

    static <E> Collection<E> InsertAll(JpaRepository<E, ?> repo, Collection<E> toInsert , RepoInsertOnDuplicateReturn<E> action){
        Set<E> inserted = new HashSet<>();
        toInsert.forEach(ti -> inserted.add(Insert(repo, ti, action)));
        return inserted;
    }

    static <E> E Insert(JpaRepository<E, ?> repo, E toInsert , RepoInsertOnDuplicateReturn<E> action){
        E success;
        try{
            success = repo.saveAndFlush(toInsert);
        }catch (DataIntegrityViolationException e){
            // on fail in duplicate getter we just want to fail the entire endpoint
            success = action.GetDuplicate(toInsert);

            // in case the method returned null (which it should never)
            if(success == null){
                e.printStackTrace();
                throw new IllegalStateException("Could not find duplicate existing asset by isin, unexpected duplicate constraint");
            }
        }

        return success;
    }
}
