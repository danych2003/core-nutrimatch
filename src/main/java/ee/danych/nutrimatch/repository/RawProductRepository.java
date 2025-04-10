package ee.danych.nutrimatch.repository;

import ee.danych.nutrimatch.entity.Element;
import ee.danych.nutrimatch.entity.Language;
import ee.danych.nutrimatch.entity.Product;
import ee.danych.nutrimatch.entity.ProductName;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class RawProductRepository {
    public List<Product> getProductsFromExcel() throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        FileInputStream file = getFileInputStream();
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        // Пропускаем заголовок
        if (rowIterator.hasNext()) rowIterator.next();
        List<Product> products = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Product product = new Product();

            // Чтение данных из строк и ячеек
            Iterator<Cell> cellIterator = row.cellIterator();

            Element element;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                ExceptionChecker exceptionCheckerNumericCell = cell::getNumericCellValue;
                ExceptionChecker exceptionCheckerStringCell = cell::getStringCellValue;
                if(exceptionCheckerStringCell.isExceptionSafe()) {
                    if(cell.getStringCellValue().trim().isEmpty() || cell.getStringCellValue().equals("-")) {
                        continue;
                    }
                }
                if(exceptionCheckerNumericCell.isExceptionSafe()) {
                    if(cell.getNumericCellValue() == 0) {
                        continue;
                    }
                }
                switch (cell.getColumnIndex()) {
                    case 0:  // Предположим, что первый столбец - это код продукта
                        try {
                            product.setCode(String.valueOf((int)cell.getNumericCellValue()));
                        } catch (IllegalStateException illegalArgumentException) {
                            product.setCode(cell.getStringCellValue());
                            break;
                        }

                        break;
                    case 1:  // Второй столбец - это название продукта
                        ProductName productNameEt = new ProductName();
                        productNameEt.setLanguage(Language.ET.name());
                        productNameEt.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameEt);
                        break;
                    case 3:  // Второй столбец - это название продукта
                        ProductName productNameRu = new ProductName();
                        productNameRu.setLanguage(Language.RU.name());
                        productNameRu.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameRu);
                        break;
                    case 2:  // Второй столбец - это название продукта
                        ProductName productNameEn = new ProductName();
                        productNameEn.setLanguage(Language.EN.name());
                        productNameEn.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameEn);
                        break;
                    case 4:
                        ProductName productNameLv = new ProductName();
                        productNameLv.setLanguage(Language.LV.name());
                        productNameLv.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameLv);
                        break;
                    case 5:
                        product.setSynonyms(cell.getStringCellValue());
                        break;
                    case 6:
                        product.setFoodGroup(cell.getStringCellValue());
                        break;
                    case 9:
                        product.setEnergy(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        break;
                    case 10:
                        element = new Element();
                        element.setName("Carbohydrates");
                        element.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(element);
                        break;
                    case 11:
                        Element fats = new Element();
                        fats.setName("Fats");
                        fats.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fats);
                        break;
                    case 12:
                        Element fibers = new Element();
                        fibers.setName("Dietary fibre");
                        fibers.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fibers);
                        break;
                    case 13:
                        Element proteins = new Element();
                        proteins.setName("Proteins");
                        proteins.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(proteins);
                        break;
                    case 14:
                        Element alcohol = new Element();
                        alcohol.setName("Alcohol");
                        alcohol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(alcohol);
                        break;
                    case 15:
                        Element water = new Element();
                        water.setName("Water");
                        water.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(water);
                        break;
                    case 16:
                        Element ash = new Element();
                        ash.setName("Ash");
                        ash.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(ash);
                        break;
                    case 17:
                        Element availableCarbs = new Element();
                        availableCarbs.setName("Available Carbohydrates");
                        availableCarbs.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(availableCarbs);
                        break;
                    case 18:
                        Element starch = new Element();
                        starch.setName("Starch");
                        starch.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(starch);
                        break;
                    case 19:
                        Element polyols = new Element();
                        polyols.setName("Polyols");
                        polyols.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(polyols);
                        break;
                    case 20:
                        Element sorbitol = new Element();
                        sorbitol.setName("Sorbitol");
                        sorbitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sorbitol);
                        break;
                    case 21:
                        Element mannitol = new Element();
                        mannitol.setName("Mannitol");
                        mannitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(mannitol);
                        break;
                    case 22:
                        Element isomalt = new Element();
                        isomalt.setName("Isomalt");
                        isomalt.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(isomalt);
                        break;
                    case 23:
                        Element maltitol = new Element();
                        maltitol.setName("Maltitol");
                        maltitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(maltitol);
                        break;
                    case 24:
                        Element lactitol = new Element();
                        lactitol.setName("Lactitol");
                        lactitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(lactitol);
                        break;
                    case 25:
                        Element xylitol = new Element();
                        xylitol.setName("Xylitol");
                        xylitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(xylitol);
                        break;
                    case 26:
                        Element erythritol = new Element();
                        erythritol.setName("Erythritol");
                        erythritol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(erythritol);
                        break;
                    case 27:
                        Element sugars = new Element();
                        sugars.setName("Sugars");
                        sugars.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sugars);
                        break;
                    case 28:
                        Element sucrose = new Element();
                        sucrose.setName("Sucrose");
                        sucrose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sucrose);
                        break;
                    case 29:
                        Element lactose = new Element();
                        lactose.setName("Lactose");
                        lactose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(lactose);
                        break;
                    case 30:
                        Element maltose = new Element();
                        maltose.setName("Maltose");
                        maltose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(maltose);
                        break;
                    case 31:
                        Element glucose = new Element();
                        glucose.setName("Glucose");
                        glucose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(glucose);
                        break;
                    case 32:
                        Element fructose = new Element();
                        fructose.setName("Fructose");
                        fructose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fructose);
                        break;
                    case 33:
                        Element galactose = new Element();
                        galactose.setName("Galactose");
                        galactose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(galactose);
                        break;
                    case 34:
                        Element fattyAcids = new Element();
                        fattyAcids.setName("Fatty acids");
                        fattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fattyAcids);
                        break;
                    case 35:  // Насышенные жирные кислоты
                        Element saturatedFattyAcids = new Element();
                        saturatedFattyAcids.setName("Saturated fatty acids");
                        saturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(saturatedFattyAcids);
                        break;
                    case 36:  // Мононенасыщенные жирные кислоты
                        Element monounsaturatedFattyAcids = new Element();
                        monounsaturatedFattyAcids.setName("Monounsaturated fatty acids");
                        monounsaturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(monounsaturatedFattyAcids);
                        break;
                    case 37:  // Полиненасыщенные жирные кислоты
                        Element polyunsaturatedFattyAcids = new Element();
                        polyunsaturatedFattyAcids.setName("Polyunsaturated fatty acids");
                        polyunsaturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(polyunsaturatedFattyAcids);
                        break;
                    case 38:  // Транс-жирные кислоты
                        Element transFattyAcids = new Element();
                        transFattyAcids.setName("Trans fatty acids");
                        transFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(transFattyAcids);
                        break;
                    case 39:  // Пальмитиновая кислота
                        Element palmiticAcid = new Element();
                        palmiticAcid.setName("Palmitic acid (C16)");
                        palmiticAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(palmiticAcid);
                        break;
                    case 40:  // Стеариновая кислота
                        Element stearicAcid = new Element();
                        stearicAcid.setName("Stearic acid (C18)");
                        stearicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(stearicAcid);
                        break;
                    case 41:  // Линолевая кислота
                        Element linoleicAcid = new Element();
                        linoleicAcid.setName("Linoleic acid (C18:2)");
                        linoleicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(linoleicAcid);
                        break;
                    case 42:  // Линоленовая кислота
                        Element linolenicAcid = new Element();
                        linolenicAcid.setName("Linolenic acid (C18:3)");
                        linolenicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(linolenicAcid);
                        break;
                    case 43:  // Холестерин
                        Element cholesterol = new Element();
                        cholesterol.setName("Cholesterol");
                        cholesterol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(cholesterol);
                        break;
                    case 44:  // Натрий
                        Element sodium = new Element();
                        sodium.setName("Sodium");
                        sodium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sodium);
                        break;
                    case 45:  // Калий
                        Element potassium = new Element();
                        potassium.setName("Potassium");
                        potassium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(potassium);
                        break;
                    case 46:  // Кальций
                        Element calcium = new Element();
                        calcium.setName("Calcium");
                        calcium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(calcium);
                        break;
                    case 47:  // Магний
                        Element magnesium = new Element();
                        magnesium.setName("Magnesium");
                        magnesium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(magnesium);
                        break;
                    case 48:  // Фосфор
                        Element phosphorus = new Element();
                        phosphorus.setName("Phosphorus");
                        phosphorus.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(phosphorus);
                        break;
                    case 49:  // Железо
                        Element iron = new Element();
                        iron.setName("Iron");
                        iron.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(iron);
                        break;
                    case 50:  // Цинк
                        Element zinc = new Element();
                        zinc.setName("Zinc");
                        zinc.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(zinc);
                        break;
                    case 51:  // Медь
                        Element copper = new Element();
                        copper.setName("Copper");
                        copper.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(copper);
                        break;
                    case 52:  // Манган
                        Element manganese = new Element();
                        manganese.setName("Manganese");
                        manganese.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(manganese);
                        break;
                    case 53:  // Йод
                        Element iodine = new Element();
                        iodine.setName("Iodine");
                        iodine.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(iodine);
                        break;
                    case 54:  // Селен
                        Element selenium = new Element();
                        selenium.setName("Selenium");
                        selenium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(selenium);
                        break;
                    case 55:  // Хром
                        Element chromium = new Element();
                        chromium.setName("Chromium");
                        chromium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(chromium);
                        break;
                    case 56:  // Никель
                        Element nickel = new Element();
                        nickel.setName("Nickel");
                        nickel.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(nickel);
                        break;
                    case 57:  // Витамин A
                        Element vitaminA = new Element();
                        vitaminA.setName("Vitamin A");
                        vitaminA.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminA);
                        break;
                    case 58:  // Ретинол
                        Element retinol = new Element();
                        retinol.setName("Retinol");
                        retinol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(retinol);
                        break;
                    case 59:  // Бета-каротин
                        Element betaCarotene = new Element();
                        betaCarotene.setName("Beta-carotene");
                        betaCarotene.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(betaCarotene);
                        break;
                    case 60:  // Витамин D
                        Element vitaminD = new Element();
                        vitaminD.setName("Vitamin D");
                        vitaminD.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminD);
                        break;
                    case 61:  // Витамин D3
                        Element vitaminD3 = new Element();
                        vitaminD3.setName("Vitamin D3");
                        vitaminD3.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminD3);
                        break;
                    case 62:  // Витамин E
                        Element vitaminE = new Element();
                        vitaminE.setName("Vitamin E");
                        vitaminE.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminE);
                        break;
                    case 63:  // Витамин K
                        Element vitaminK = new Element();
                        vitaminK.setName("Vitamin K");
                        vitaminK.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminK);
                        break;
                    case 64:  // Витамин B1
                        Element vitaminB1 = new Element();
                        vitaminB1.setName("Vitamin B1");
                        vitaminB1.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB1);
                        break;
                    case 65:  // Витамин B2
                        Element vitaminB2 = new Element();
                        vitaminB2.setName("Vitamin B2");
                        vitaminB2.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB2);
                        break;
                    case 66:  // Niacin equivalents, total, niacin equivalent
                        Element niacinEquivalents = new Element();
                        niacinEquivalents.setName("Niacin equivalents, total");
                        niacinEquivalents.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacinEquivalents);
                        break;

                    case 67:  // Niacin, milligram (сам ниацин)
                        Element niacin = new Element();
                        niacin.setName("Niacin");
                        niacin.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacin);
                        break;

                    case 68:  // Niacin equiv. from tryptophan, milligram (ниацин из триптофана)
                        Element niacinFromTryptophan = new Element();
                        niacinFromTryptophan.setName("Niacin from Tryptophan");
                        niacinFromTryptophan.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacinFromTryptophan);
                        break;
                    case 69:  // Витамин B6
                        Element pathonicAcid = new Element();
                        pathonicAcid.setName("Pantothenic acid");
                        pathonicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(pathonicAcid);
                        break;
                    case 70:  // Витамин B6
                        Element vitaminB6 = new Element();
                        vitaminB6.setName("Vitamin B6");
                        vitaminB6.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB6);
                        break;
                    case 71:  // Фолаты
                        Element biotin = new Element();
                        biotin.setName("Biotin");
                        biotin.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(biotin);
                        break;
                    case 73:  // Витамин B12
                        Element vitaminB12 = new Element();
                        vitaminB12.setName("Vitamin B12");
                        vitaminB12.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB12);
                        break;
                    case 72:  // Фолаты
                        Element folates = new Element();
                        folates.setName("Folate");
                        folates.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(folates);
                        break;
                    case 74:  // Витамин C
                        Element vitaminC = new Element();
                        vitaminC.setName("Vitamin C");
                        vitaminC.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminC);
                        break;
                    case 75:  // Эквивалент соли
                        Element saltEquivalent = new Element();
                        saltEquivalent.setName("Salt equivalent");
                        saltEquivalent.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(saltEquivalent);
                        break;
                }
            }
            products.add(product);
        }

        workbook.close();
        file.close();
        return products;
    }
    private static FileInputStream getFileInputStream() throws FileNotFoundException {
        return new FileInputStream("tmp/export.xlsx");
    }

}
