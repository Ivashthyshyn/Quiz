package com.example.key.quiz.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

@Entity(active = true, nameInDb = "QUESTION")
public class Question {
    @Id
    private long id;
    @NotNull
    @Unique
    private long communicationId;

    @NotNull
    private long type;

    @NotNull
    private String questions;

    @NotNull
    private long rightAnswerId;

    @ToMany(joinProperties = {
            @JoinProperty(name = "communicationId",referencedName = "remoutCommunicationId")
    })
    private List<Answer> answers;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 891254763)
    private transient QuestionDao myDao;




    @Generated(hash = 1185162632)
    public Question(long id, long communicationId, long type, @NotNull String questions,
            long rightAnswerId) {
        this.id = id;
        this.communicationId = communicationId;
        this.type = type;
        this.questions = questions;
        this.rightAnswerId = rightAnswerId;
    }
    @Generated(hash = 1868476517)
    public Question() {
    }




    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return this.type;
    }

    public void setType(long type) {
        this.type = type;
    }
    public String getQuestions() {
        return this.questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
    public void setRightAnswer(long rightAnswerId) {
        this.rightAnswerId = rightAnswerId;
    }
    public long getRightAnswer() {
        return this.rightAnswerId;
    }
    public long getCommunicationId() {
        return this.communicationId;
    }
    public void setCommunicationId(long communicationId) {
        this.communicationId = communicationId;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1142007765)
    public List<Answer> getAnswers() {
        if (answers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerDao targetDao = daoSession.getAnswerDao();
            List<Answer> answersNew = targetDao._queryQuestion_Answers(communicationId);
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
    public long getRightAnswerId() {
        return this.rightAnswerId;
    }
    public void setRightAnswerId(int rightAnswerId) {
        this.rightAnswerId = rightAnswerId;
    }
    public void setRightAnswerId(long rightAnswerId) {
        this.rightAnswerId = rightAnswerId;
    }

    
}
