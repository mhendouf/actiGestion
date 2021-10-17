package org.sid.repository;

import java.util.Date;
import java.util.List;

import org.sid.entity.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PassageRepository extends JpaRepository<Passage, Long> {
	@Query(value = "SELECT passage FROM Passage passage where MONTH( passage.passage_date )= MONTH( :passageDate ) and passage.idBenevole= :idBenevole")
	List<Passage> findPassagesByDate(Date passageDate, Long idBenevole);

	@Query(value = "SELECT passage FROM Passage passage where MONTH( passage.passage_date )= MONTH( :passageDate ) and DAY( passage.passage_date )= DAY( :passageDate ) and YEAR( passage.passage_date )= YEAR( :passageDate ) and passage.idBenevole= :idBenevole")
	Passage findPassage(Date passageDate, Long idBenevole);

	@Query(value = "SELECT passage FROM Passage passage where passage.idBenevole= :idBenevole")
	List<Passage> findListPassage(Long idBenevole);

	@Query(value = "SELECT passage FROM Passage passage where passage.idBenevole= :idBenevole AND MONTH(passage.passage_date)= MONTh(:d) AND YEAR(passage.passage_date)= YEAR(:dy) ")
	List<Passage> findNbByMonth(Long idBenevole, Date d, Date dy);

	@Query(value = "SELECT passage FROM Passage passage where passage.idActi= :idActi")
	Passage findByIdActi(Long idActi);
}
