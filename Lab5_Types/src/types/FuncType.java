package types;

public class FuncType extends Type {
   
   private TypeList args;
   private Type ret;
   
   public FuncType(TypeList args, Type returnType)
   {
      this.args = args;
      this.ret = returnType;
   }
   
   public Type returnType()
   {
      return ret;
   }
   
   public TypeList arguments()
   {
      return args;
   }
   
   @Override
   public String toString()
   {
      return "func(" + args + "):" + ret;
   }
   @Override
   public Type call(Type args)
   {
	   if(!(this.args.equivalent(args)))
		   return super.call(args);
	   return ret;
   }
   @Override
   public Type add(Type that) {
	   if(!(this.equivalent2(that)))
   		return super.add(that);
	   	return returnType();
   }
   @Override
   public Type sub(Type that) {
	   if(!(this.equivalent2(that)))
   		return super.sub(that);
	   	return returnType();
   }
   @Override
   public Type mul(Type that) {
	   if(!(this.equivalent2(that)))
   		return super.mul(that);
	   	return returnType();
   }
   @Override
   public Type div(Type that) {
	   if(!(this.equivalent2(that)))
   			return super.div(that);
   	return returnType();
   }
   @Override
   public Type compare(Type that) {
	   if(!(this.equivalent2(that)))
		  return super.compare(that);
   	return new BoolType();
   }
   public Type mainCheck() {
	   if(!(ret instanceof VoidType))
		   return new ErrorType("Function main has invalid signature.");
	   return new VoidType();
   }
   @Override
   public boolean equivalent(Type that)
   {
      if (that == null)
         return false;
      if (!(that instanceof FuncType))
         return false;
      
      FuncType aType = (FuncType)that;
      return this.ret.equivalent(aType.ret) && this.args.equivalent(aType.args);
   }
   // just return types need to be equivalent
   public boolean equivalent2(Type that)
   {
	      if (that == null)
	          return false;
	       if (!(that instanceof FuncType))
	          return false;
	       
	       FuncType aType = (FuncType)that;
	       return this.ret.equivalent(aType.ret);
   }
}
