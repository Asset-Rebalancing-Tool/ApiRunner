package ARApi.Scaffold.Database.Entities;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface DuplicateAwareInserter<T> {

    /**
     * This Method is passed as a lambda implementation to the static insert functions.
     * Should fetch and return the existing entry based on the duplicate parameter
     * @param duplicateEntity the duplicate parameter
     * @return already existing instance fetched from db
     */
    T FetchExisting(T duplicateEntity);

    static <E> Collection<E> InsertAll(JpaRepository<E, ?> repo, Collection<E> toInsert , DuplicateAwareInserter<E> action){
        Set<E> inserted = new HashSet<>();
        toInsert.forEach(ti -> inserted.add(Insert(repo, ti, action)));
        return inserted;
    }

    static <E> E Insert(JpaRepository<E, ?> repo, E toInsert , DuplicateAwareInserter<E> action){
        E success;
        try{
            success = repo.saveAndFlush(toInsert);
        }catch (DataIntegrityViolationException e){
            // on fail in duplicate getter we just want to fail the entire endpoint
            success = action.FetchExisting(toInsert);

            // in case the method returned null (which it should never)
            if(success == null){
                e.printStackTrace();
                throw new IllegalStateException("Could not find duplicate existing asset by isin, unexpected duplicate constraint");
            }
        }

        return success;
    }
}
