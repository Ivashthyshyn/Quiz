package com.example.key.quiz.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity(active = true, nameInDb = "QUESTION")
public class Question {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private int type;

    @NotNull
    private int level;

    @NotNull
    private String questions;

    @NotNull
    private String rightAnswer;

    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "questionId")
    })
    private List<Answer> answers;

    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "userQuestionId")
    })
    private List<UserSuccess> userAnswers;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 891254763)
private transient QuestionDao myDao;

@Generated(hash = 332543999)
public Question(Long id, int type, int level, @NotNull String questions,
        @NotNull String rightAnswer) {
    this.id = id;
    this.type = type;
    this.level = level;
    this.questions = questions;
    this.rightAnswer = rightAnswer;
}

@Generated(hash = 1868476517)
public Question() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public int getType() {
    return this.type;
}

public void setType(int type) {
    this.type = type;
}

public int getLevel() {
    return this.level;
}

public void setLevel(int level) {
    this.level = level;
}

public String getQuestions() {
    return this.questions;
}

public void setQuestions(String questions) {
    this.questions = questions;
}

public String getRightAnswer() {
    return this.rightAnswer;
}

public void setRightAnswer(String rightAnswer) {
    this.rightAnswer = rightAnswer;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1548987448)
public List<Answer> getAnswers() {
    if (answers == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        AnswerDao targetDao = daoSession.getAnswerDao();
        List<Answer> answersNew = targetDao._queryQuestion_Answers(id);
        synchronized (this) {
            if (answers == null) {
                answers = answersNew;
            }
        }
    }
    return answers;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 663994474)
public synchronized void resetAnswers() {
    answers = null;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1131512743)
public List<UserSuccess> getUserAnswers() {
    if (userAnswers == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        UserSuccessDao targetDao = daoSession.getUserSuccessDao();
        List<UserSuccess> userAnswersNew = targetDao
                ._queryQuestion_UserAnswers(id);
        synchronized (this) {
            if (userAnswers == null) {
                userAnswers = userAnswersNew;
            }
        }
    }
    return userAnswers;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1278706619)
public synchronized void resetUserAnswers() {
    userAnswers = null;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 754833738)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getQuestionDao() : null;
}

}