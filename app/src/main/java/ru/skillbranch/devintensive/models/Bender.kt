package ru.skillbranch.devintensive.models

import android.util.Log

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion() : String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (valid, invalidMessage) = question.isValid(answer)
        return if (!valid) {
            "${invalidMessage}\n${question.question}" to status.color
        } else {
            if (question.answers.contains(answer.toLowerCase())) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                if (status == Status.NORMAL) {
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else
                    "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            Log.d("M_Bender", "${this.ordinal} ${values().lastIndex}")
            return if(this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            }
            else
            {
//                values()[values().lastIndex]
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun isValid(inp: String): Pair<Boolean, String> {
                return if (inp.first().isUpperCase()) true to ""
                else false to "Имя должно начинаться с заглавной буквы"
            }
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun isValid(inp: String): Pair<Boolean, String> {
                return if (inp.first().isLowerCase()) true to ""
                else false to "Профессия должна начинаться со строчной буквы"
            }

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun isValid(inp: String): Pair<Boolean, String> {
                return if (!inp.contains(Regex("\\d"))) true to ""
                else false to "Материал не должен содержать цифр"
            }

            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun isValid(inp: String): Pair<Boolean, String> {
                return if (inp.matches(Regex("\\d+"))) true to ""
                else false to "Год моего рождения должен содержать только цифры"
            }

            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun isValid(inp: String): Pair<Boolean, String> {
                return if (inp.matches(Regex("\\d{7}"))) true to ""
                else false to "Серийный номер содержит только цифры, и их 7"
            }

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun isValid(inp: String): Pair<Boolean, String> = true to ""
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion():Question
        abstract fun isValid(inp: String): Pair<Boolean, String>
    }
}