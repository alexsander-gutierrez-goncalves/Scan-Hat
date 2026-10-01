package br.com.alexsander.leitor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.data.CodeDAO
import br.com.alexsander.leitor.repository.CodeRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var codeDao: CodeDAO
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<TestDatabase>(context).build()
        codeDao = db.codeDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun should_Add_New_Entity_To_Database() = runTest {
        val testCode = "Scan Hat"
        val code = Code(value = testCode)
        val repository = CodeRepositoryImpl(codeDao)
        repository.insert(code)
        val item = repository.codes.first().firstOrNull()
        item?.let {
            assertEquals(testCode, item.value)
        }
    }

    @Test
    fun should_Delete_New_Entity_To_Database() = runTest {
        val testCode = "Scan Hat"
        val code = Code(value = testCode)
        val repository = CodeRepositoryImpl(codeDao)
        repository.delete(code)
        val codes = repository.codes.first()
        println(codes.size)
        assertEquals(true, codes.isEmpty())
    }

}

@Database(entities = [Code::class], version = 1)
abstract class TestDatabase : RoomDatabase() {
    abstract fun codeDao(): CodeDAO
}