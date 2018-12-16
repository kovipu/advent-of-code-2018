const fs = require('fs');


fs.readFile('input.txt', 'utf8', (err, data) => {
  if (err) throw err;

  const occurences = data.split('\n')
    .map(line => appearsTwiceOrThrice(line))

  const result = occurences
    .reduce((acc, occurences) => ({
      two: occurences.two ? acc.two + 1 : acc.two,
      three: occurences.three ? acc.three + 1 : acc.three
    }), {
      two: 0,
      three: 0
    });

  console.log(result.two * result.three)
});

const appearsTwiceOrThrice = (line) => {
  const occurences = countOccurences(line);

  return Object.keys(occurences).reduce((acc, key) => ({
    two: occurences[key] === 2 ? true : acc.two,
    three: occurences[key] === 3 ? true : acc.three
  }), {
    two: false,
    three: false
  })
};

const countOccurences = (line) => line.split('')
  .reduce((acc, val) => ({
    ...acc,
    [val]: acc[val]
      ? acc[val] + 1
      : 1
    }), {});
