def main():
    acc = 0
    with open('input.txt') as f:
        input_file = f.readlines()

    for line in input_file:
        value = int(line[1:])
        if line.startswith('+'):
            acc += value
        else:
            acc -= value

    print(acc)

if __name__ == '__main__':
    main()