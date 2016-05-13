package dk.frankbille.mysqldumper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TableTest {

    @Test
    public void treeSizeWithDirectCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        table1.addDependentTable(table1);

        assertThat(table1.getTreeSize()).isEqualTo(100);
    }

    @Test
    public void treeSizeWithLargeCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        Table table2 = new Table("table2", 200);
        Table table3 = new Table("table3", 300);
        table1.addDependentTable(table2);
        table2.addDependentTable(table3);
        table3.addDependentTable(table1);

        assertThat(table1.getTreeSize()).isEqualTo(600);
    }

    @Test
    public void setDataIncludedWithDirectCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        table1.addDependentTable(table1);

        table1.setDataIncluded(false);

        assertThat(table1.isDataIncluded()).isFalse();
    }

    @Test
    public void setDataIncludedWithLargeCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        Table table2 = new Table("table2", 200);
        Table table3 = new Table("table3", 300);
        table1.addDependentTable(table2);
        table2.addDependentTable(table3);
        table3.addDependentTable(table1);

        table1.setDataIncluded(false);

        assertThat(table1.isDataIncluded()).isFalse();
        assertThat(table2.isDataIncluded()).isFalse();
        assertThat(table3.isDataIncluded()).isFalse();
    }

    @Test
    public void setDataIncludedWithComplexCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        Table table2 = new Table("table2", 200);
        Table table3 = new Table("table3", 300);
        table1.addDependentTable(table2);
        table2.addDependentTable(table2);
        table2.addDependentTable(table3);
        table3.addDependentTable(table1);

        table1.setDataIncluded(false);

        assertThat(table1.isDataIncluded()).isFalse();
        assertThat(table2.isDataIncluded()).isFalse();
        assertThat(table3.isDataIncluded()).isFalse();
    }

    @Test
    public void setStructureIncludedWithDirectCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        table1.addDependentTable(table1);

        table1.setStructureIncluded(false);

        assertThat(table1.isStructureIncluded()).isFalse();
    }

    @Test
    public void setStructureIncludedWithLargeCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        Table table2 = new Table("table2", 200);
        Table table3 = new Table("table3", 300);
        table1.addDependentTable(table2);
        table2.addDependentTable(table3);
        table3.addDependentTable(table1);

        table1.setStructureIncluded(false);

        assertThat(table1.isStructureIncluded()).isFalse();
        assertThat(table2.isStructureIncluded()).isFalse();
        assertThat(table3.isStructureIncluded()).isFalse();
    }

    @Test
    public void setStructureIncludedWithComplexCircularTableReferences() {
        Table table1 = new Table("table1", 100);
        Table table2 = new Table("table2", 200);
        Table table3 = new Table("table3", 300);
        table1.addDependentTable(table2);
        table2.addDependentTable(table2);
        table2.addDependentTable(table3);
        table3.addDependentTable(table1);

        table1.setStructureIncluded(false);

        assertThat(table1.isStructureIncluded()).isFalse();
        assertThat(table2.isStructureIncluded()).isFalse();
        assertThat(table3.isStructureIncluded()).isFalse();
    }

}