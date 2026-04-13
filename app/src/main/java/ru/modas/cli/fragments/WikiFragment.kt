package ru.modas.cli.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import ru.modas.cli.R

class WikiFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var contentContainer: LinearLayout
    private lateinit var scrollView: NestedScrollView

    // Данные для вики
    private val wikiData = mapOf(
        "классы" to WikiArticle(
            title = "Классы персонажей",
            content = """
                В Dungeons & Dragons существует множество классов персонажей, каждый со своими уникальными способностями:
                
                • Воин (Fighter) - Мастер боя, владеющий всеми видами оружия и доспехов. Отличается высокой выживаемостью и множеством атак.
                
                • Волшебник (Wizard) - Могучий заклинатель, черпающий магию из изученных книг. Может специализироваться на различных школах магии.
                
                • Плут (Rogue) - Скрытный и ловкий специалист по взлому замков, обезвреживанию ловушек и нанесению критических ударов из тени.
                
                • Жрец (Cleric) - Священный воин, черпающий силу от своего божества. Может как исцелять союзников, так и наносить урон врагам.
                
                • Следопыт (Ranger) - Искусный охотник и следопыт, специализирующийся на выживании в дикой природе и стрельбе из лука.
                
                • Паладин (Paladin) - Священный рыцарь, сочетающий боевые навыки с божественной магией. Клятва паладина определяет его путь.
                
                • Варвар (Barbarian) - Дикий воин, впадающий в ярость и наносящий сокрушительные удары. Обладает огромным запасом здоровья.
                
                • Бард (Bard) - Вдохновляющий музыкант и рассказчик, поддерживающий союзников песнями и владеющий разнообразной магией.
                
                • Друид (Druid) - Хранитель природы, способный принимать облик животных и повелевать силами стихий.
                
                • Монах (Monk) - Мастер боевых искусств, использующий энергию ци для сверхъестественных способностей.
                
                • Колдун (Warlock) - Заключивший договор с могущественным существом, получающий от него тайные знания и магию.
                
                • Чародей (Sorcerer) - Носитель врождённой магии в крови, способный спонтанно применять заклинания.
            """.trimIndent()
        ),
        "расы" to WikiArticle(
            title = "Расы персонажей",
            content = """
                В мире D&D существует множество рас, каждая со своими особенностями:
                
                • Люди (Human) - Амбициозные и разносторонние, люди быстро адаптируются к любым условиям.
                
                • Эльфы (Elf) - Древняя раса, известная своей грацией, красотой и долголетием. Отличные лучники и маги.
                
                • Дварфы (Dwarf) - Коренастые и выносливые мастера кузнечного дела и воины, устойчивые к ядам и магии.
                
                • Полурослики (Halfling) - Маленькие и удачливые существа, искусные в скрытности и воровстве.
                
                • Гномы (Gnome) - Маленькие изобретатели и иллюзионисты, полные энергии и любопытства.
                
                • Драконорождённые (Dragonborn) - Гордые потомки драконов, способные извергать стихийную энергию.
                
                • Тифлинги (Tiefling) - Носители демонической крови, обладающие магическими способностями и необычной внешностью.
                
                • Полуорки (Half-Orc) - Сильные и выносливые воины, унаследовавшие ярость от орков и хитрость от людей.
                
                • Полуэльфы (Half-Elf) - Дети людей и эльфов, сочетающие лучшие черты обеих рас.
                
                • Ааракокра (Aarakocra) - Птицелюди, способные летать и живущие в высокогорьях.
            """.trimIndent()
        ),
        "атрибуты" to WikiArticle(
            title = "Атрибуты персонажа",
            content = """
                Шесть основных атрибутов определяют возможности персонажа:
                
                • Сила (Strength) - Физическая мощь, способность поднимать тяжести, наносить удары в ближнем бою.
                Модификатор силы влияет на атаки тяжёлым оружием и проверки атлетики.
                
                • Ловкость (Dexterity) - Координация, рефлексы, равновесие. Влияет на инициативу, класс доспеха,
                атаки дальнобойным и лёгким оружием, а также на спасброски от области поражения.
                
                • Телосложение (Constitution) - Выносливость и здоровье. Определяет количество хитов
                и влияет на спасброски от ядов и болезней.
                
                • Интеллект (Intelligence) - Обучаемость, память, логическое мышление. Важен для волшебников
                и влияет на проверки знаний и расследование.
                
                • Мудрость (Wisdom) - Интуиция, внимательность, сила воли. Важна для жрецов, друидов и следопытов.
                Влияет на внимательность и проницательность.
                
                • Харизма (Charisma) - Сила личности, обаяние, лидерство. Важна для бардов, паладинов и колдунов.
                Влияет на убеждение, запугивание и обман.
                
                Формула модификатора: (значение атрибута - 10) / 2 (округление вниз)
            """.trimIndent()
        ),
        "магия" to WikiArticle(
            title = "Магия и заклинания",
            content = """
                Основы магии в D&D:
                
                • Уровни заклинаний - Заклинания делятся на уровни от 0 (заговоры) до 9.
                Чем выше уровень, тем мощнее эффект, но и получить его сложнее.
                
                • Слоты заклинаний - Ограниченные ресурсы для применения заклинаний.
                После использования слот восстанавливается после отдыха.
                
                • Заговоры (Cantrips) - Заклинания 0 уровня, которые можно использовать без ограничений.
                
                • Классы заклинателей - Волшебники, колдуны, чародеи, жрецы, друиды, барды, паладины и следопыты.
                
                • Сложность спасброска - DC (Difficulty Class) определяет, насколько сложно
                цели избежать эффекта заклинания.
                
                • Концентрация - Некоторые заклинания требуют поддержания концентрации.
                При получении урона нужно совершить спасбросок, чтобы не потерять заклинание.
                
                • Школы магии - Абjuration (Ограждение), Conjuration (Вызов), Divination (Прорицание),
                Enchantment (Очарование), Evocation (Воплощение), Illusion (Иллюзия), Necromancy (Некромантия),
                Transmutation (Превращение).
            """.trimIndent()
        ),
        "боевая система" to WikiArticle(
            title = "Боевая система",
            content = """
                Как работает бой в D&D:
                
                • Инициатива - Порядок ходов определяется броском к20 + модификатор ловкости.
                
                • Действия в бою:
                  - Атака (Attack) - Нанесение удара оружием или заклинанием.
                  - Засада (Ready) - Подготовка действия к определённому триггеру.
                  - Помощь (Help) - Помощь союзнику в атаке или проверке.
                  - Отход (Disengage) - Безопасное перемещение без провоцирования атак.
                  - Рывок (Dash) - Дополнительное перемещение.
                  - Уклонение (Dodge) - Преимущество на спасброски ловкости.
                  - Засада (Hide) - Попытка спрятаться.
                  - Использование предмета (Use an Object) - Использование предмета.
                
                • Класс Доспеха (AC) - Сложность попадания по персонажу.
                Чем выше AC, тем сложнее врагу нанести удар.
                
                • Критические попадания - При выпадении 20 на кубе d20,
                атака автоматически попадает и наносит двойной урон.
                
                • Критические провалы - При выпадении 1 атака автоматически промахивается.
                
                • Спасброски - Бросок куба против эффекта заклинания или ловушки.
                
                • Преимущество/Помеха - Ситуации, когда бросается два куба d20
                и выбирается лучший (преимущество) или худший (помеха) результат.
            """.trimIndent()
        ),
        "снаряжение" to WikiArticle(
            title = "Снаряжение и предметы",
            content = """
                Типы снаряжения в D&D:
                
                • Оружие:
                  - Простое: дубина, кинжал, копьё, праща, лёгкий арбалет
                  - Воинское: длинный меч, большой меч, длинный лук, боевой молот
                  - Экзотическое: цеп, сеть, духовое ружьё
                
                • Доспехи:
                  - Лёгкие: стёганый, кожаный
                  - Средние: чешуйчатый, кираса
                  - Тяжёлые: кольчуга, латы
                  - Щиты: дают бонус +2 к AC
                
                • Магические предметы:
                  - Обычные: зелья лечения, свитки заклинаний
                  - Необычные: +1 оружие, кольцо защиты
                  - Редкие: плащ невидимости, летающие сапоги
                  - Очень редкие: +3 доспехи, куб силы
                  - Легендарные: артефакты, разящий молот
                
                • Расходные материалы:
                  - Зелья лечения (2d4+2 хитов)
                  - Яды (различные эффекты)
                  - Святые символы (для жрецов и паладинов)
                  - Компоненты для заклинаний
                
                • Инструменты:
                  - Воровские инструменты (для плутов)
                  - Алхимический набор
                  - Музыкальные инструменты (для бардов)
                  - Кузнечные инструменты
            """.trimIndent()
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wiki, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupSearch()
        loadWikiContent()
    }

    private fun initViews(view: View) {
        searchEditText = view.findViewById(R.id.searchEditText)
        contentContainer = view.findViewById(R.id.contentContainer)
        scrollView = view.findViewById(R.id.scrollView)
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.lowercase() ?: ""
                if (query.isEmpty()) {
                    loadWikiContent()
                } else {
                    searchWiki(query)
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun loadWikiContent() {
        contentContainer.removeAllViews()

        wikiData.forEach { (key, article) ->
            addArticleToContainer(article)
        }

        addSeparator()
        addInfoSection()
    }

    private fun searchWiki(query: String) {
        contentContainer.removeAllViews()

        val results = wikiData.filter { (key, article) ->
            article.title.lowercase().contains(query) ||
                    article.content.lowercase().contains(query)
        }

        if (results.isNotEmpty()) {
            results.forEach { (key, article) ->
                val highlightedTitle = highlightText(article.title, query)
                val highlightedContent = highlightText(article.content, query)

                val highlightedArticle = WikiArticle(highlightedTitle, highlightedContent)
                addArticleToContainer(highlightedArticle)
            }
        } else {
            addNoResultsMessage(query)
        }
    }

    private fun highlightText(text: String, query: String): String {
        if (query.isEmpty()) return text

        val regex = Regex("(${Regex.escape(query)})", RegexOption.IGNORE_CASE)
        return regex.replace(text) {
            "<font color='#FFD700'><b>${it.value}</b></font>"
        }
    }

    private fun getTextColor(): Int {
        // Получаем цвет текста из темы
        val typedValue = android.util.TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        return ContextCompat.getColor(requireContext(), typedValue.resourceId)
    }

    private fun getDividerColor(): Int {
        // Получаем цвет разделителя из темы
        val typedValue = android.util.TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.textColorHint, typedValue, true)
        return ContextCompat.getColor(requireContext(), typedValue.resourceId)
    }

    private fun addArticleToContainer(article: WikiArticle) {
        val textColor = getTextColor()
        val dividerColor = getDividerColor()

        // Заголовок статьи
        val titleView = TextView(requireContext()).apply {
            text = if (article.title.contains("<font")) {
                Html.fromHtml(article.title, Html.FROM_HTML_MODE_LEGACY)
            } else {
                article.title
            }
            textSize = 22f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setTextColor(textColor)
            setPadding(0, 32, 0, 16)
        }
        contentContainer.addView(titleView)

        // Разделитель
        val divider = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            setBackgroundColor(dividerColor)
        }
        contentContainer.addView(divider)

        // Содержание статьи
        val contentView = TextView(requireContext()).apply {
            text = if (article.content.contains("<font")) {
                Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                article.content
            }
            textSize = 16f
            setTextColor(textColor)
            setPadding(0, 16, 0, 32)
            movementMethod = ScrollingMovementMethod()
        }
        contentContainer.addView(contentView)
    }

    private fun addSeparator() {
        val dividerColor = getDividerColor()

        val separator = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            setBackgroundColor(dividerColor)
            setPadding(0, 16, 0, 16)
        }
        contentContainer.addView(separator)
    }

    private fun addInfoSection() {
        val textColor = getTextColor()

        val infoTitle = TextView(requireContext()).apply {
            text = "Полезные советы"
            textSize = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setTextColor(textColor)
            setPadding(0, 24, 0, 16)
        }
        contentContainer.addView(infoTitle)

        val infoContent = TextView(requireContext()).apply {
            text = """
                • Всегда носите с собой зелья лечения - они могут спасти жизнь
                • Используйте окружение в бою: укрытия, высота, ловушки
                • Команда, работающая слаженно, побеждает даже сильных врагов
                • Изучайте слабости монстров перед битвой
                • Не забывайте про навыки: иногда разговор решает больше, чем меч
                • Отдых важен - после битвы обязательно восстанавливайте силы
                • Экспериментируйте с заклинаниями и тактиками
                • Ведите записи о важных NPC и квестах
            """.trimIndent()
            textSize = 14f
            setTextColor(textColor)
            setPadding(0, 0, 0, 32)
        }
        contentContainer.addView(infoContent)
    }

    private fun addNoResultsMessage(query: String) {
        val textColor = getTextColor()

        val messageView = TextView(requireContext()).apply {
            text = "По запросу \"$query\" ничего не найдено.\n\nПопробуйте поискать по словам:\nклассы, расы, атрибуты, магия, боевая система, снаряжение"
            textSize = 16f
            setTextColor(textColor)
            gravity = android.view.Gravity.CENTER
            setPadding(32, 100, 32, 0)
        }
        contentContainer.addView(messageView)
    }

    data class WikiArticle(
        val title: String,
        val content: String
    )
}